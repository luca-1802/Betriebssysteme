#! /usr/bin/bash

echo "übergebene Kommandozeilenargumente:"
echo $*
echo "Versionsnummer Linux Kernel:"
uname --kernel-version
echo
echo "Anzahl CPU Kerne:"
grep -a "cpu cores" /proc/cpuinfo
echo
echo "Menge freier Hauptspeicher:"
grep -i "MemFree" /proc/meminfo
echo
echo "IP-Adresse des Rechners:"
hostname --ip-address
echo
echo "angemeldete Nutzer:"
who --users
echo
echo "Belegter Speicherplatz:"
du -hs .
exit
