#include <stdio.h>
#include <unistd.h>
#include <errno.h>
#include <bpf/bpf.h>

int main()
{
    char cong[5][9] = {"reno", "cubic", "illinois", "vegas", "westwood"};
    char *pval = cong[0];
    char *ipval = cong[1];
    unsigned int cong_map_fd;

    cong_map_fd = bpf_obj_get("/sys/fs/bpf/cong_map");
    int key1 = 443, key2 = 80;
    // Update map elements
    bpf_map_update_elem(cong_map_fd, &key1, pval, BPF_ANY);
    bpf_map_update_elem(cong_map_fd, &key2, ipval, BPF_ANY);

    printf("Updated 'cong_map' and 'ip_cong_map' successfully\n");
}