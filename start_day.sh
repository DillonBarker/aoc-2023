#!/bin/bash

# Check if an argument is provided
if [ $# -eq 0 ]; then
    echo "Usage: $0 <day_number>"
    exit 1
fi

# Get the day number from the command line argument
day_number=$1
year=2023

# Define source and destination directories
src_dir="src/main/kotlin/net/dill/y$year"
template_dir="src/main/kotlin/net/dill/template"
day_dir="$src_dir/day$day_number"
kt_file="$day_dir/Day${day_number}.kt"

# Create the destination directory
mkdir -p "$day_dir"

# Copy and rename template files
cp "$template_dir/DayX.txt" "$day_dir/Day${day_number}.txt"
cp "$template_dir/DayX_a_test.txt" "$day_dir/Day${day_number}_a_test.txt"
cp "$template_dir/DayX_b_test.txt" "$day_dir/Day${day_number}_b_test.txt"
cp "$template_dir/DayX.kt" "$kt_file"

# Replace "X" with the day number in the .kt file
sed -i "s/DAY = \"dayX\"/DAY = \"day${day_number}\"/" "$kt_file"
# Replace "template" with the day number in the .kt file
sed -i "s/package net.dill.template/package net.dill.y${year}.day${day_number}/" "$kt_file"

# Display success message
echo "day$day_number folder created with template files in $src_dir."
