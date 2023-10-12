#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <time.h>
#include <dirent.h>

#include "main.h"

// ! remove
#include <stdio.h>
#include <string.h>


struct stat getStat(char *filename)
{
    struct stat fileStat;
    stat(filename, &fileStat);
    return fileStat;
}

char* getPerms(struct stat *fileStat) {
    char perms[11] = "";

    
    unsigned int mode = fileStat->st_mode;

    // -- directory
    if S_ISDIR(mode) {
        strcat(perms, "d");
    }
    else {
        strcat(perms, "-");
    }

    // -- USER
    if (mode & S_IRUSR) {
        strcat(perms, "r");
    } else {
        strcat(perms, "-");
    }
    if (mode & S_IWUSR) {
        strcat(perms, "w");
    } else {
        strcat(perms, "-");
    }
    if (mode & S_IXUSR) {
        strcat(perms, "x");
    } else {
        strcat(perms, "-");
    }
    
    // -- GROUP
    if (mode & S_IRGRP) {
        strcat(perms, "r");
    } else {
        strcat(perms, "-");
    }
    if (mode & S_IWGRP) {
        strcat(perms, "w");
    } else {
        strcat(perms, "-");
    }
    if (mode & S_IXGRP) {
        strcat(perms, "x");
    } else {
        strcat(perms, "-");
    }

    // -- OTHER
    if (mode & S_IROTH) {
        strcat(perms, "r");
    } else {
        strcat(perms, "-");
    }
    if (mode & S_IWOTH) {
        strcat(perms, "w");
    } else {
        strcat(perms, "-");
    }
    if (mode & S_IXOTH) {
        strcat(perms, "x");
    } else {
        strcat(perms, "-");
    }

    return perms;
}

char *itoa(int num, char *str) {
    int i = 0;
    int isNegative = 0;

    if (num == 0) {
        str[i++] = '0';
        str[i] = '\0';
        return str;
    }

    while (num != 0) {
        int remainder = num % 10; // -- 10 cos base 10
        str[i++] = (remainder > 9) ? (remainder - 10) + 'a' : remainder + '0'; // -- assign correct offsets to number to get characters
        num = num / 10; // -- again 10 cos base 10
    }

    str[i] = '\0'; // -- DONT FORGET TO NULL TERMINATE!!!!!!

    int start = 0;
    int end = strlen(str);
    while (start < end) {
        char temp = str[start];
        str[start] = str[end];
        str[end] = temp;
        end--;
        start++;
    }

    return str;
}

char** getDirContents(char *dirName) {
    // int dirFd = open(dirName);

    
}

// based on an input file name
char* makeFileEntry(char *fileName) {
    struct stat fileStat = getStat(fileName);
    // get perms
    char *perms = getPerms(&fileStat);
    // get number of hard links to file
    int numberOfLinks = fileStat.st_nlink;
    // get owner name
    int ownerID = fileStat.st_uid;
    // get owner group
    int ownerGroup = fileStat.st_gid;
    // get file size in bytes
    int fileSizeBytes = fileStat.st_size;
    // time since last modification
    int timeOfLastModificaion = fileStat.st_mtime;
    // file name
    // given as input param

    // convert ints to strings
    char *strOfLinks;
    strOfLinks = itoa(numberOfLinks, strOfLinks);
    char *strOfOwnerID;
    strOfOwnerID = itoa(ownerID, strOfOwnerID);
    char *strOfGroupID;
    strOfGroupID = itoa(ownerGroup,strOfGroupID);
    char *strOfFileSizeBytes;
    strOfFileSizeBytes = itoa(fileSizeBytes, strOfFileSizeBytes);
    // char *strOfLastModification = itoa(timeOfLastModifica) // !! actually want to calculate time with datetime and figure out if modification was longer ago than a year

    printf("str of links: %s\n", strOfLinks);
}

int main(int args, char **argv)
{
    // !! Check that args have been passed

    char *filename = argv[1];
    struct stat fileStat = getStat(filename);
    printf("%u\n", fileStat.st_mode);

    if (S_ISREG(fileStat.st_mode))
    {
        printf("regular file\n");
        char* permsString = getPerms(&fileStat);
        printf("%s", permsString);
        makeFileEntry(filename);
    }
    else if (S_ISDIR(fileStat.st_mode))
    {
        printf("directory\n");
        char* permsString = getPerms(&fileStat);
        printf("%s\n", permsString);
    }
    else
    {
        printf("other\n");
    }

    return 0;
}
