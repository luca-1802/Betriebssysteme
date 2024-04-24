#!/bin/bash

# Überprüfen, ob ein Verzeichnis als Argument übergeben wurde
if [ -z "$1" ]; then
    echo "Verwendung: teil2.sh <verzeichnis>"
    exit 1
fi

# Das Verzeichnis, welches durchsucht werden soll
directory=$1

# Schleife über alle Files im angegebenen Verzeichnis
for file in "$directory"/*; do
	# Überprüfen, ob es sich um eine Datei handelt
	if [ -f "$file" ]; then
		# Den Datei Typen ermitteln 
		filetype=$(file -b "$file")
		echo "$file: $filetype"
	fi
done