/* Starter code for a system tool using only system calls. In this practical, we will make no use of
 * standard C library calls -- we will only use system calls provided by the Linux kernel, and do
 * everything else ourselves. The only exception is localtime(), which can be used to convert seconds
 * to sensible dates.
 *
 * Kasim Terzic, Chris Brown, 2018-2023
 *
 * See:
 *
 *  https://blog.packagecloud.io/eng/2016/04/05/the-definitive-guide-to-linux-system-calls/
 *  https://en.wikibooks.org/wiki/X86_Assembly/Interfacing_with_Linux
 *  http://ibiblio.org/gferg/ldp/GCC-Inline-Assembly-HOWTO.html
 *
 *  man 2 syscalls
 *  man 2 stat opendir readdir write localtime inode getdents
 */

#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <time.h>
#include <dirent.h>

// A complete list of linux system call numbers can be found in: /usr/include/asm/unistd_64.h
#define WRITE_SYSCALL 1

int main(int argc, char **argv)
{
    // This contains four different ways to make the "write" system call. You will be doing
    // one of these variations in your submission to ask the kernel to perform tasks for you.
    // For the purposes of this practical, the asm versions are considered better because they
    // make interaction with the kernel more explicit.

    // I am using long ints here to avoid converting them for asm (rxx registers are 64 bits)

    char *text = "Hello world! We come in peace!\n"; // String to print
    size_t len = 31;                                 // Length of our string, which we need to pass to write syscall
    long handle = 1;                                 // 1 for stdout, 2 for stderr, file handle from open() for files
    long ret = -1;                                   // Return value received from the system call

    // First way is to use the wrapper function defined in POSIX. This is the "normal" way
    // of doing things. You may wish to use this in the first instance to get a basic working
    // solution before refactoring it to call the system calls directly
    ret = write(handle, text, len);

    // Using the Linux syscall function for generic calls to the kernel
    // Note -- first argument is the system call number for write. Also note how similar
    // this is to the wrapper above
    ret = syscall(WRITE_SYSCALL, handle, text, len);

    // Using inline assembler, the long way. Here all the registers used are visible
    // Note that we have to protect rax etc. in the very last line to stop the gnu compiler
    // from using them (and thus overwriting our data)
    // Parameters go to RAX, RDI, RSI, RDX, in that order
    asm("movq %1, %%rax\n\t"
        "movq %2, %%rdi\n\t"
        "movq %3, %%rsi\n\t"
        "movq %4, %%rdx\n\t"
        "syscall\n\t"
        "movq %%rax, %0\n\t" : "=r"(ret) : "r"((long)WRITE_SYSCALL), "r"(handle), "r"(text), "r"(len) : "%rax", "%rdi", "%rsi", "%rdx", "memory");

    // Using inline assembler with shortcuts.
    // The registers are the same as above. We use a constraint to force variables into particular registers:
    // a = rax, D = rdi, S = rsi, d = rdx... This is a property of GNU inline assembler.
    // The only asm instruction we have to execute is syscall, since all the arguments are in the right registers
    asm("syscall" : "=a"(ret) : "0"(WRITE_SYSCALL), "D"(handle), "S"(text), "d"(len) : "cc", "rcx", "r11", "memory");

    return ret;
}
