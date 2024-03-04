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

SEC("sockops")
int bpf_cong(struct bpf_sock_ops *skops)
{
    char cong[] = "reno";
    char currCC[20];
    int rv = 0;
    int op;
    op = (int)skops->op;

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
        rv = bpf_setsockopt(skops, SOL_TCP, TCP_CONGESTION,
                            cong, sizeof(cong));
        break;
    case BPF_SOCK_OPS_PASSIVE_ESTABLISHED_CB:
        rv = bpf_setsockopt(skops, SOL_TCP, TCP_CONGESTION,
                            cong, sizeof(cong));
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