EXE = Critical_Path
CC 	= g++
GCC_OPTS=-c -O3 -Wall -Wextra -m64
SRC = Critical_Path.cpp ReadProjectData.cpp ScheduleJobs.cpp job.cpp
OBJ = $(SRC:.cpp=.o) 

$(EXE): $(OBJ)
	$(CC) $(OBJ) -o $(EXE) 

.o:.cpp
	$(CC) $(GCC_OPTS) $@ -o $<

clean:
	rm -f *.o $(EXE)

