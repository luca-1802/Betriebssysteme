#!/bin/bash

# Eine Funktion für farbigen Output
colored_header() {
	printf "\e[1;34m$1\e[1;0m"
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
printf "$(colored_header "Übergebene Argumente[$args]:")\n$all_args\n\n"

# Versionsnummer des Linux-Kernels
kernel_version=$(uname -r)
printf "$(colored_header "Linux-Kernel-Version:")\n$kernel_version\n\n"

# Anzahl der CPU-Kerne
cpu_cores=$(cat /proc/cpuinfo | grep -c '^cpu cores' | uniq)
printf "$(colored_header "Anzahl der CPU-Kerne:")\n$cpu_cores\n\n"

# Menge des freien Hauptspeichers
free_memory=$(grep -oP 'MemFree:\s+\K\d+' /proc/meminfo)
printf "$(colored_header "Freier Hauptspeicher:")\n$free_memory kB\n\n"

# IP-Adresse des Rechners
ip_address=$(hostname -i)
printf "$(colored_header "IP-Adresse des Rechners:")\n$ip_address\n\n"

# Liste der angemeldeten Nutzer
logged_in_users=$(who)
printf "$(colored_header "Angemeldete Nutzer:")\n$logged_in_users\n\n"

# Belegter Speicherplatz im aktuellen Verzeichnis
used_space=$(du -sh . | awk '{print $1}')
printf "$(colored_header "Belegter Speicherplatz im aktuellen Verzeichnis:")\n$used_space\n\n"