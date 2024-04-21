#!/bin/bash

# Alle übergebenen Kommandozeilenargumente
args=0
all_args=""

for arg in "$@"; do
	all_args+="$arg, "
	args=$((args+1))
done

# Das letzte ", " entfernen
all_args="${all_args%, }"

# Ausgeben der Argumente
echo "Übergebene Argumente[$args]: $all_args"

# Versionsnummer des Linux-Kernels
kernel_version=$(uname -r)
echo "Linux-Kernel-Version: $kernel_version"

# Anzahl der CPU-Kerne
cpu_cores=$(cat /proc/cpuinfo | grep -c '^cpu cores' | uniq)
echo "Anzahl der CPU-Kerne: $cpu_cores"

# Menge des freien Hauptspeichers
free_memory=$(grep -oP 'MemFree:\s+\K\d+' /proc/meminfo)
echo "Freier Hauptspeicher: $free_memory kB"

# IP-Adresse des Rechners
ip_address=$(hostname -I)
echo "IP-Adresse des Rechners: $ip_address"

# Liste der angemeldeten Nutzer
logged_in_users=$(who | awk '{print $1}')
echo "Angemeldete Nutzer: $logged_in_users"

# Belegter Speicherplatz im aktuellen Verzeichnis
used_space=$(du -sh . | awk '{print $1}')
echo "Belegter Speicherplatz im aktuellen Verzeichnis: $used_space"
