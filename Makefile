JAVAC = \javac
JAVA = \java
SOURCE = Solver.java
MAIN = MasterMind
ERROR = /dev/null
DIR1 = reidai01
DIR2 = reidai02
DIR3 = reidai03

.SILENT : 
all : 
	echo ${DIR1}
	cp ./${SOURCE} ${DIR1}
	(\cd ${DIR1}; ${JAVAC} ${SOURCE} && ${JAVA} ${MAIN}; \rm ${SOURCE})
	\echo ;\echo ${DIR2}
	cp ./${SOURCE} ${DIR2}
	(\cd ${DIR2}; ${JAVAC} ${SOURCE} && ${JAVA} ${MAIN}; \rm ${SOURCE})
	\echo ;\echo ${DIR3}
	cp ./${SOURCE} ${DIR3}
	(\cd ${DIR3}; ${JAVAC} ${SOURCE} && ${JAVA} ${MAIN}; \rm ${SOURCE})
	\echo
