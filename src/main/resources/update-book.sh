#!/bin/bash
python3 convert.py mappings-en_us.json |python3 generate_book.py - en_us_base.json > assets/psi/lang/en_us.json
