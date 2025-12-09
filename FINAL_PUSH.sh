#!/usr/bin/env bash
# Final push script using HTTPS with gh CLI (GitHub CLI) or git credential

set -euo pipefail

cd /home/omar/Documents/jarvis

echo "=== Final Push to GitHub ==="
echo ""
echo "Status:"
git status --short
echo ""

echo "Recent commits:"
git log --oneline | head -3
echo ""

echo "Checking for GitHub CLI..."
if command -v gh >/dev/null 2>&1; then
    echo "GitHub CLI found. Using 'gh' for authentication..."
    gh auth status || gh auth login
    git push -u origin main
else
    echo "GitHub CLI not found."
    echo ""
    echo "Option 1: Use HTTPS with Personal Access Token"
    echo "  - Create token at: https://github.com/settings/tokens/new"
    echo "  - Scopes needed: repo, workflow"
    echo "  - Then run: git push -u origin main"
    echo "  - When prompted for password, paste your token"
    echo ""
    echo "Option 2: Set up SSH (recommended for future use)"
    echo "  - Create SSH key: ssh-keygen -t ed25519 -C 'your-email@example.com'"
    echo "  - Add to GitHub: https://github.com/settings/ssh/new"
    echo "  - Then: git remote set-url origin git@github.com:slto7-code-fixer/jarvis.git"
    echo "  - Then: git push -u origin main"
    echo ""
    
    read -p "Enter your choice (1 for HTTPS token, 2 for SSH, or press Ctrl+C to cancel): " choice
    
    if [ "$choice" = "1" ]; then
        git push -u origin main
    elif [ "$choice" = "2" ]; then
        git remote set-url origin git@github.com:slto7-code-fixer/jarvis.git
        git push -u origin main
    else
        echo "Cancelled."
        exit 1
    fi
fi

echo ""
echo "=== Push Complete! ==="
echo "Check GitHub Actions: https://github.com/slto7-code-fixer/jarvis/actions"
