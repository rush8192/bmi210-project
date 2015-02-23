
cd bin/
if [ $# -ge 1 ]; then
	java $@
else
	java Main "../stub_ontology.ont"
fi
