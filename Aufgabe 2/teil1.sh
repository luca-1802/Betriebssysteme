#!/bin/bash

# Eine Funktion für den Output
output() {
    printf "$1\n$2\n\n"
}

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
output "Übergebene Argumente[$args]:" "$all_args"

# Versionsnummer des Linux-Kernels
kernel_version=$(uname -r)
advanced_info=$(uname --kernel-version)
output "Linux-Kernel-Version:" "$kernel_version | $advanced_info"

# Anzahl der CPU-Kerne
cpu_cores=$(cat /proc/cpuinfo | grep -a '^cpu cores' | sed -E 's/.*: ([0-9]+)/\1/' | uniq)
output "Anzahl der CPU-Kerne:" "$cpu_cores"

# Menge des freien Hauptspeichers
free_memory=$(grep -oP 'MemFree:\s+\K\d+' /proc/meminfo)
output "Freier Hauptspeicher:" "$free_memory kB"

# IP-Adresse des Rechners
ip_address=$(hostname --ip-address)
output "IP-Adresse des Rechners:" "$ip_address"

# Liste der angemeldeten Nutzer
logged_in_users=$(who --users)
output "Angemeldete Nutzer:" "$logged_in_users"

# Belegter Speicherplatz im aktuellen Verzeichnis
used_space=$(du -sh . | awk '{print $1}')
output "Belegter Speicherplatz im aktuellen Verzeichnis:" "$used_space"
