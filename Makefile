TARGETS = changeCC

all: $(TARGETS)
.PHONY: all

$(TARGETS): %: %.bpf.o 

%.bpf.o: %.bpf.c
	clang \
	    -target bpf \
		-I/usr/include/$(shell uname -m)-linux-gnu \
		-g \
	    -O2 -o $@ -c $<

bpf-load:
	bpftool prog load changeCC.bpf.o /sys/fs/bpf/hello

bpf-attach:
	bpftool cgroup attach "/sys/fs/cgroup/" sock_ops pinned "/sys/fs/bpf/hello"

bpf-detach:
	bpftool cgroup detach "/sys/fs/cgroup/" sock_ops pinned "/sys/fs/bpf/hello"

bpf-trace:
	bpftool prog tracelog

clean: 
	- rm *.bpf.o
	- rm /sys/fs/bpf/hello
