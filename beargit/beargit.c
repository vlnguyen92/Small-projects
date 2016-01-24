#include <stdio.h>
#include <string.h>

#include <unistd.h>
#include <sys/stat.h>

#include "beargit.h"
#include "util.h"

/* Implementation Notes:
 *
 * - Functions return 0 if successful, 1 if there is an error.
 * - All error conditions in the function description need to be implemented
 *   and written to stderr. We catch some additional errors for you in main.c.
 * - Output to stdout needs to be exactly as specified in the function description.
 * - Only edit this file (beargit.c)
 * - Here are some of the helper functions from util.h:
 *   * fs_mkdir(dirname): create directory <dirname>
 *   * fs_rm(filename): delete file <filename>
 *   * fs_mv(src,dst): move file <src> to <dst>, overwriting <dst> if it exists
 *   * fs_cp(src,dst): copy file <src> to <dst>, overwriting <dst> if it exists
 *   * write_string_to_file(filename,str): write <str> to filename (overwriting contents)
 *   * read_string_from_file(filename,str,size): read a string of at most <size> (incl.
 *     NULL character) from file <filename> and store it into <str>. Note that <str>
 *     needs to be large enough to hold that string.
 *  - You NEED to test your code. The autograder we provide does not contain the
 *    full set of tests that we will run on your code. See "Step 5" in the project spec.
 */

/* beargit init
 *
 * - Create .beargit directory
 * - Create empty .beargit/.index file
 * - Create .beargit/.prev file containing 0..0 commit id
 *
 * Output (to stdout):
 * - None if successful
 */

int beargit_init(void) {
    fs_mkdir(".beargit");

    FILE* findex = fopen(".beargit/.index", "w");
    fclose(findex);

    FILE* fbranches = fopen(".beargit/.branches", "w");
    fprintf(fbranches, "%s\n", "master");
    fclose(fbranches);

    write_string_to_file(".beargit/.prev", "0000000000000000000000000000000000000000");
    write_string_to_file(".beargit/.current_branch", "master");

    return 0;
}



/* beargit add <filename>
 *
 * - Append filename to list in .beargit/.index if it isn't in there yet
 *
 * Possible errors (to stderr):
 * >> ERROR:  File <filename> has already been added.
 *
 * Output (to stdout):
 * - None if successful
 */

int beargit_add(const char* filename) {
    FILE* findex = fopen(".beargit/.index", "r");
    FILE *fnewindex = fopen(".beargit/.newindex", "w");

    char line[FILENAME_SIZE];
    while(fgets(line, sizeof(line), findex)) {
        strtok(line, "\n");
        if (strcmp(line, filename) == 0) {
            fprintf(stderr, "ERROR:  File %s has already been added.\n", filename);
            fclose(findex);
            fclose(fnewindex);
            fs_rm(".beargit/.newindex");
            return 3;
        }

        fprintf(fnewindex, "%s\n", line);
    }

    fprintf(fnewindex, "%s\n", filename);
    fclose(findex);
    fclose(fnewindex);

    fs_mv(".beargit/.newindex", ".beargit/.index");

    return 0;
}

/* beargit status
 *
 * See "Step 1" in the project spec.
 *
 */

int beargit_status() {
    FILE* findex = fopen(".beargit/.index", "r");
    char line[FILENAME_SIZE];
    printf("Tracked files:\n\n");

    int cnt = 0;
    while(fgets(line, sizeof(line), findex))
      {
        printf("%s",line);
        cnt++;
      }
    printf("\nThere are %d files total.\n",cnt);
    fclose(findex);
    return 0;
}

/* beargit rm <filename>
 *
 * See "Step 2" in the project spec.
 *
 */

int beargit_rm(const char* filename) {
    /* COMPLETE THE REST */
    FILE* findex = fopen(".beargit/.index", "r");
    FILE* fNewIndex = fopen(".beargit/.newindex", "w");
    char line[FILENAME_SIZE];
    int found = 0;
    int cnt = 0;
    while(fgets(line, sizeof(line), findex))
      {
        strtok(line,"\n");
        if(strcmp(line,filename) == 0)
          found = 1;
        else
          fprintf(fNewIndex,"%s",line);
        cnt++;
      }
    if(found == 0) fprintf(stderr,"ERROR: File %s not tracked.\n",filename);
    if(cnt > 1) fprintf(fNewIndex,"%s","\n");
    fclose(findex);
    fclose(fNewIndex);

    fs_mv(".beargit/.newindex", ".beargit/.index");
    return 0;
}

/* beargit commit -m <msg>
 *
 * See "Step 3" in the project spec.
 *
 */

const char* go_bears = "THIS IS BEAR TERRITORY!";

int is_commit_msg_ok(const char* msg) {
    //TODO: Doesn't work for last char next to the !
    int equal = 0;
    int i;
    for(i = 0; i < MSG_SIZE && *(msg+i); i++)
      {
        int j = 0;
        while(*(go_bears+j))
          {
            char msgChar = *(msg+i+j);
            char bearChar = *(go_bears + j);
            if(msgChar != bearChar)
              break;
            else
              j++;
          }
        if(!*(go_bears+j))
          {
            equal = 1;
            break;
          }
        i+=j;
      }
    if(equal) return 1;

    return 0;
}

/* Use next_commit_id to fill in the rest of the commit ID.
 *
 * Hints:
 * You will need a destination string buffer to hold your next_commit_id, before you copy it back to commit_id
 * You will need to use a function we have provided for you.
 */

void next_commit_id(char* commit_id) {
    /* COMPLETE THE REST */
    char buff[SHA_HEX_BYTES+1];
    cryptohash(commit_id,buff);
    int i;
    for(i = 0; i < SHA_HEX_BYTES+1;i++)
      *(commit_id + i) = buff[i];
}

int beargit_commit(const char* msg) {
    if (!is_commit_msg_ok(msg)) {
        fprintf(stderr, "ERROR:  Message must contain \"%s\"\n", go_bears);
        return 1;
    }

    char currBranch[BRANCHNAME_SIZE];
    read_string_from_file(".beargit/.current_branch",currBranch,BRANCHNAME_SIZE);
    if(strlen(currBranch) == 0) 
      {
        fprintf(stderr, "ERROR:  Need to be on HEAD of a branch to commit.\n");
        return 1;
      }

    char commit_id[COMMIT_ID_SIZE];
    read_string_from_file(".beargit/.prev", commit_id, COMMIT_ID_SIZE);
    next_commit_id(commit_id);
    char dirStr[COMMIT_ID_SIZE+11];
    memset(dirStr,0,COMMIT_ID_SIZE+11);
    strcat(dirStr,".beargit/.");
    strcat(dirStr,commit_id);
    fs_mkdir(dirStr);
    strcat(dirStr,"/");
    char baseStr[COMMIT_ID_SIZE+11];
    strcpy(baseStr,dirStr);
    fs_cp(".beargit/.index",strcat(dirStr,".index"));
    strcpy(dirStr,baseStr);
    fs_cp(".beargit/.prev",strcat(dirStr,".prev"));
    strcpy(dirStr,baseStr);
    write_string_to_file(strcat(dirStr,".msg"),msg);
    strcpy(dirStr,baseStr);

    FILE *findex = fopen(".beargit/.index","r");
    char line[FILENAME_SIZE];
    while(fgets(line,sizeof(line),findex))
      {
        strtok(line,"\n");
        strcpy(dirStr,baseStr);
        fs_cp(line,strcat(dirStr,line));
      }
    fclose(findex);    

    write_string_to_file(".beargit/.newprev",commit_id);
    //    printf("%s\n",commit_id);
    fs_mv(".beargit/.newprev", ".beargit/.prev");

    return 0;
}

/* beargit log
 *
 * See "Step 4" in the project spec.
 *
 */

int beargit_log(int limit) {
    /* COMPLETE THE REST */
    char commit[COMMIT_ID_SIZE];
    char msg[MSG_SIZE];
    memset(msg,0,MSG_SIZE);
    //    read_string_from_file(".beargit/.prev",commit,COMMIT_ID_SIZE);
    char firstCommit[COMMIT_ID_SIZE] = "0000000000000000000000000000000000000000";
    char dir[COMMIT_ID_SIZE+FILENAME_SIZE];
    memset(dir,0,COMMIT_ID_SIZE+FILENAME_SIZE);
    strcat(dir,".beargit/.");
    char baseStr[COMMIT_ID_SIZE+11];
    strcpy(baseStr,dir);
    strcat(strcat(dir,commit),"prev");
    read_string_from_file(dir,commit,COMMIT_ID_SIZE);
    if(strcmp(commit,firstCommit) == 0)
      fprintf(stderr,"ERROR:  There are no commits.\n");
    while(strcmp(commit,firstCommit) != 0)
      {
        strcpy(dir,baseStr);
        strcat(strcat(dir,commit),"/.msg");
        read_string_from_file(dir,msg,MSG_SIZE);
        printf("commit %s\n",commit);
        printf("   %s\n",msg);
        strcpy(dir,baseStr);
        strcat(strcat(dir,commit),"/.prev");
        read_string_from_file(dir,commit,COMMIT_ID_SIZE);
        printf("\n");
      }
    return 0;
}


// This helper function returns the branch number for a specific branch, or
// returns -1 if the branch does not exist.
int get_branch_number(const char* branch_name) {
    FILE* fbranches = fopen(".beargit/.branches", "r");

    int branch_index = -1;
    int counter = 0;
    char line[FILENAME_SIZE];
    while(fgets(line, sizeof(line), fbranches)) {
        strtok(line, "\n");
        if (strcmp(line, branch_name) == 0) {
            branch_index = counter;
        }
        counter++;
    }

    fclose(fbranches);

    return branch_index;
}

/* beargit branch
 *
 * See "Step 5" in the project spec.
 *
 */

int beargit_branch() {
    /* COMPLETE THE REST */
    FILE* fbranch= fopen(".beargit/.branches", "r");
    char line[BRANCHNAME_SIZE];
    char currBranch[BRANCHNAME_SIZE];
    read_string_from_file(".beargit/.current_branch",currBranch,BRANCHNAME_SIZE);

    while(fgets(line, sizeof(line), fbranch))
      {
        strtok(line,"\n");
        if(strcmp(line,currBranch) == 0) printf("*  ");
        else
          printf("   ");
        printf("%s",line);
        printf("\n");
      }
    fclose(fbranch);

    return 0;
}

/* beargit checkout
 *
 * See "Step 6" in the project spec.
 *
 */

int checkout_commit(const char* commit_id) {
    /* COMPLETE THE REST */
    char currCommit[COMMIT_ID_SIZE];
    strcpy(currCommit,commit_id);
    if(strcmp(commit_id,"0000000000000000000000000000000000000000") == 0)
      write_string_to_file(".beargit/.index","");
    else 
      {
        FILE* findex= fopen(".beargit/.index", "r");
        //    strcat(commit,commit_id);

        char line[FILENAME_SIZE + BRANCHNAME_SIZE];
        while(fgets(line, sizeof(line), findex))
          {
            strtok(line,"\n");
            //        printf("%s",line);
            fs_rm(line);
          }
        fclose(findex);

        char indexFile[FILENAME_SIZE];
        sprintf(indexFile,".beargit/.%s%s",currCommit,"/.index");
        //    printf("%s\n",indexFile);
        fs_cp(indexFile,".beargit/.index");
        findex = fopen(".beargit/.index","r");
        while(fgets(line, sizeof(line), findex))
          {
            strtok(line,"\n");
            sprintf(indexFile,".beargit/.%s/%s",currCommit,line);
            fs_cp(indexFile,line);
          }
        fclose(findex);
      }
    write_string_to_file(".beargit/.prev",currCommit);


    return 0;
}

int is_it_a_commit_id(const char* commit_id) {
    /* COMPLETE THE REST */
    char commit_dir[FILENAME_SIZE] = ".beargit/";
    strcat(commit_dir,".");
    strcat(commit_dir,commit_id);
    //    printf("%s\n",commit_dir);
    //    printf("%d\n",fs_check_dir_exists(commit_dir));
    if(fs_check_dir_exists(commit_dir)) return 1;
}

int beargit_checkout(const char* arg, int new_branch) {
    // Get the current branch
    char current_branch[BRANCHNAME_SIZE];
    read_string_from_file(".beargit/.current_branch", current_branch, BRANCHNAME_SIZE);

    // If not detached, leave the current branch by storing the current HEAD into that branch's file...
    if (strlen(current_branch)) {
        char current_branch_file[BRANCHNAME_SIZE+50];
        sprintf(current_branch_file, ".beargit/.branch_%s", current_branch);
        fs_cp(".beargit/.prev", current_branch_file);
    }

    // Check whether the argument is a commit ID. If yes, we just change to detached mode
    // without actually having to change into any other branch.
    int isCommit = is_it_a_commit_id(arg);
    if (isCommit) {
        char commit_dir[FILENAME_SIZE] = ".beargit/";
        strcat(commit_dir, arg);
        // ...and setting the current branch to none (i.e., detached).
        write_string_to_file(".beargit/.current_branch", "");

        return checkout_commit(arg);
    }



    // Read branches file (giving us the HEAD commit id for that branch).
    int branch_exists = (get_branch_number(arg) >= 0);

    // Check for errors.
    if (branch_exists && new_branch) {
        fprintf(stderr, "ERROR:  A branch named %s already exists.\n", arg);
        return 1;
    } else if (!branch_exists && !new_branch) {
        fprintf(stderr, "ERROR:  No branch or commit %s exists.\n", arg);
        return 1;
    }

    if(!isCommit && !branch_exists && !new_branch)
      {
        fprintf(stderr,"EROR: No branch or commit %s exists.",arg);
        return 1;

      }

    // Just a better name, since we now know the argument is a branch name.
    const char* branch_name = arg;

    // File for the branch we are changing into.
    char branch_file[BRANCHNAME_SIZE + 50] = ".beargit/.branch_";
    strcat(branch_file, branch_name);

    // Update the branch file if new branch is created (now it can't go wrong anymore)
    if (new_branch) {
        FILE* fbranches = fopen(".beargit/.branches", "a");
        fprintf(fbranches, "%s\n", branch_name);
        fclose(fbranches);
        fs_cp(".beargit/.prev", branch_file);
    }

    write_string_to_file(".beargit/.current_branch", branch_name);

    // Read the head commit ID of this branch.
    char branch_head_commit_id[COMMIT_ID_SIZE];
    read_string_from_file(branch_file, branch_head_commit_id, COMMIT_ID_SIZE);

    // Check out the actual commit.
    return checkout_commit(branch_head_commit_id);
}

/* beargit reset
 *
 * See "Step 7" in the project spec.
 *
 */

int beargit_reset(const char* commit_id, const char* filename) {
    if (!is_it_a_commit_id(commit_id)) {
        fprintf(stderr, "ERROR:  Commit %s does not exist.\n", commit_id);
        return 1;
    }

    // Check if the file is in the commit directory
    /* COMPLETE THIS PART */
    char commitDir[COMMIT_ID_SIZE + FILENAME_SIZE + 11];
    sprintf(commitDir,".beargit/.%s/%s",commit_id,".index");

    FILE *findex = fopen(commitDir,"r");
    char line[FILENAME_SIZE];
    int found = 0;

    while(fgets(line,sizeof(line),findex))
      {
        strtok(line,"\n");
        if(strcmp(line,filename) == 0)
          {
            found = 1;
            break;
          }
      }
    if(!found)
      fprintf(stderr,"ERROR:  %s is not in the index of commit %s.\n",filename,commit_id);

    // Copy the file to the current working directory
    /* COMPLETE THIS PART */

    // Add the file if it wasn't already in the current directory
    /* COMPLETE THIS PART */
    fs_cp(commitDir,filename);

    return 0;
}

/* beargit merge
 *
 * See "Step 8" in the project spec.
 *
 */

int beargit_merge(const char* arg) {
    // Get the commit_id or throw an error
    char commit_id[COMMIT_ID_SIZE];
    if (!is_it_a_commit_id(arg)) {
        if (get_branch_number(arg) == -1) {
            fprintf(stderr, "ERROR:  No branch or commit %s exists.\n", arg);
            return 1;
        }
        char branch_file[FILENAME_SIZE];
        snprintf(branch_file, FILENAME_SIZE, ".beargit/.branch_%s", arg);
        read_string_from_file(branch_file, commit_id, COMMIT_ID_SIZE);
    } else {
        snprintf(commit_id, COMMIT_ID_SIZE, "%s", arg);
    }
    char commitIndex[FILENAME_SIZE + COMMIT_ID_SIZE];
    sprintf(commitIndex,".beargit/.%s/.index",commit_id);
    char *currIndex = ".beargit/.index";

    char lineCommit[FILENAME_SIZE]; //tracked file in commit
    FILE *commitFile = fopen(commitIndex,"r");
    while(fgets(lineCommit,sizeof(lineCommit),commitFile))
      {
        strtok(lineCommit,"\n");
        FILE *currFile = fopen(currIndex,"r");
        char lineCurr[FILENAME_SIZE];//tracked file in current directory
        while(fgets(lineCurr,sizeof(lineCurr),currFile))
          {
            strtok(lineCurr,"\n");
            //if the file is also tracked in current directory
            if(strcmp(lineCurr,lineCommit) == 0)
              {
                char commitFile[FILENAME_SIZE + COMMIT_ID_SIZE * 2];
                sprintf(commitFile,".beargit/.%s/%s",commit_id,lineCommit);
                fs_cp(commitFile,strcat(strcat(lineCurr,"."),commit_id));
                printf("%s conflicted copy created\n",lineCurr);
              }
            else
              {
                char commitFile[FILENAME_SIZE + COMMIT_ID_SIZE * 2];
                sprintf(commitFile,".beargit/.%s/%s",commit_id,lineCommit);
                fs_cp(commitFile,lineCommit);
//                fprintf(currFile, "%s\n", lineCommit);
                printf("%s added\n",lineCommit);
              }
          }
        fclose(currFile);
      }
    fclose(commitFile);

    // Iterate through each line of the commit_id index and determine how you
    // should copy the index file over
    /* COMPLETE THE REST */

    return 0;
}
