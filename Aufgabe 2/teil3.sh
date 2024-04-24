#!/bin/bash

# Überprüfen, ob ein Verzeichnis als Argument übergeben wurde
if [ -z "$1" ]; then
    echo "Verwendung: teil3.sh <verzeichnis>"
    exit 1
fi

directory="$1"

# Funktion zum Durchsuchen des Verzeichnisses
search_directory() {
    local temp_directory="$1"
    for file in "$temp_directory"/*; do
        # Überprüfen, ob es sich um eine Datei handelt
        if [ -f "$file" ]; then
            # Den Dateityp ermitteln
            filetype=$(file -b "$file")
            echo "$file: $filetype"
        elif [ -d "$file" ]; then
            # Wenn es sich um ein Verzeichnis handelt, die Funktion rekursiv aufrufen
            search_directory "$file"
        fi
    done
}

# Erster Aufruf der Funktion mit dem übergebenen Verzeichnisnamen
search_directory "$directory"