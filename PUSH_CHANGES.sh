#!/usr/bin/env bash
# Simple script to push local changes to GitHub
# Just run: ./PUSH_CHANGES.sh

set -euo pipefail

echo "=== Git Push Helper ==="
echo ""
echo "1) Checking git status..."
git status --short

echo ""
echo "2) Verifying remote..."
git remote -v

echo ""
echo "3) Pushing main branch to GitHub..."
echo "   You may be prompted to enter your GitHub credentials or PAT."
echo ""
git push -u origin main

echo ""
echo "=== Success! ==="
echo "Your changes have been pushed to GitHub."
echo "Check GitHub Actions at: https://github.com/slto7-code-fixer/jarvis/actions"
echo "APK will be available after the build completes."
