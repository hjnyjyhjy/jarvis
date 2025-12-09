#!/usr/bin/env bash
# Push to GitHub using HTTPS with credential helper
# This script will prompt for GitHub credentials once, then cache them

set -euo pipefail

cd /home/omar/Documents/jarvis

echo "Setting up git credential helper (cache credentials temporarily)..."
git config --global credential.helper cache

echo ""
echo "Switching remote to HTTPS..."
git remote set-url origin https://github.com/slto7-code-fixer/jarvis.git

echo ""
echo "Attempting to push to GitHub..."
echo "When prompted, enter:"
echo "  - Username: your GitHub username"
echo "  - Password: Your Personal Access Token (not your GitHub password)"
echo ""
echo "To create a PAT: https://github.com/settings/tokens/new"
echo "Required scopes: repo, workflow"
echo ""

git push -u origin main

echo ""
echo "✅ Push successful!"
echo "Check GitHub: https://github.com/slto7-code-fixer/jarvis"
echo "Watch Actions: https://github.com/slto7-code-fixer/jarvis/actions"
