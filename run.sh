
./build.sh
if [ $? -ne 0 ]; then
	exit 1
fi

cd bin/
if [ $# -ge 1 ]; then
	java $@
else
	java Main "../stub_ontology.ont"
fi
