#include <stdio.h>
#include <unistd.h>
#include <errno.h>
#include <bpf/bpf.h>

int main(int argc, char *argv[])
{
    if (argc != 2)
    {
        fprintf(stderr, "Usage: %s <string_value>\n", argv[0]);
        return 1;
    }
    char *pval = argv[1];
    unsigned int cong_map_fd;
    cong_map_fd = bpf_obj_get("/sys/fs/bpf/cong_map");
    int key = 1;
    // Update map elements
    bpf_map_update_elem(cong_map_fd, &key, pval, BPF_ANY);
    printf("Updated 'cong_map' and 'ip_cong_map' successfully\n");
}