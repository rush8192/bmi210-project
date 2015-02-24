
./build.sh
if [ $? -ne 0 ]; then
	exit 1
fi

DATA_DIR="data/"


if [ $# -ge 1 ]; then
	java -cp "bin/" $@
else
	java -cp "bin/" Main $DATA_DIR"210-ont.csv" $DATA_DIR"210-dis-symp.csv" $DATA_DIR"210-symp-syn.csv"
fi
