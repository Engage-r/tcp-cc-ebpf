#include <linux/bpf.h>
#include <linux/tcp.h>
#include <linux/if_ether.h>
#include <linux/if_packet.h>
#include <linux/ip.h>
#include <linux/socket.h>
#include <bpf/bpf_helpers.h>
#include <bpf/bpf_endian.h>

#define DEBUG 1
#define SOL_TCP 6
#define AF_INET6 10

struct
{
    __uint(type, BPF_MAP_TYPE_HASH);
    __uint(key_size, sizeof(__u32));
    __uint(value_size, 10);
    __uint(max_entries, 1024);
    __uint(pinning, LIBBPF_PIN_BY_NAME);
} cong_map SEC(".maps");

SEC("sockops")
int bpf_cong(struct bpf_sock_ops *skops)
{
    char currCC[20];
    int rv = 0;
    int op;
    op = (int)skops->op;
    long dport = (long)bpf_ntohl(skops->remote_port);
    char *ip_con_str;

#ifdef DEBUG
    bpf_printk("BPF command: %d\n", op);
#endif

    bpf_getsockopt(skops, SOL_TCP, TCP_CONGESTION, currCC, sizeof(currCC));
    bpf_printk("congestion control algo before:%s \n", currCC);
    switch (op)
    {
    case BPF_SOCK_OPS_NEEDS_ECN:
        rv = 1;
        break;
    case BPF_SOCK_OPS_ACTIVE_ESTABLISHED_CB:
    case BPF_SOCK_OPS_PASSIVE_ESTABLISHED_CB:
        ip_con_str = bpf_map_lookup_elem(&cong_map, &dport);
        if (ip_con_str == NULL)
            return 1;
        rv = bpf_setsockopt(skops, SOL_TCP, TCP_CONGESTION,
                            ip_con_str, sizeof(ip_con_str));
        break;
    default:
        rv = -1;
    }
    bpf_getsockopt(skops, SOL_TCP, TCP_CONGESTION, currCC, sizeof(currCC));
    bpf_printk("congestion control algo after:%s \n", currCC);
#ifdef DEBUG
    bpf_printk("Returning %d\n", rv);
#endif
    skops->reply = rv;
    return 1;
}
char _license[] SEC("license") = "GPL";