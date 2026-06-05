#!/bin/bash

# Codemagic Setup Helper Script for Tindroid
# This script helps configure your local environment and prepare for Codemagic CI/CD

set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_ROOT="$SCRIPT_DIR"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}Tindroid Codemagic Setup Helper${NC}"
echo "=================================="

# Function to check if file exists
check_file() {
    if [ -f "$1" ]; then
        echo -e "${GREEN}✓${NC} Found: $1"
        return 0
    else
        echo -e "${RED}✗${NC} Missing: $1"
        return 1
    fi
}

# Function to check command exists
check_command() {
    if command -v "$1" &> /dev/null; then
        echo -e "${GREEN}✓${NC} Found: $1"
        return 0
    else
        echo -e "${RED}✗${NC} Missing: $1"
        return 1
    fi
}

echo -e "\n${YELLOW}Step 1: Checking Prerequisites${NC}"
echo "-------------------------------"

check_command "java"
check_command "gradle"
check_command "git"

echo -e "\n${YELLOW}Step 2: Checking Project Structure${NC}"
echo "-----------------------------------"

check_file "$PROJECT_ROOT/build.gradle"
check_file "$PROJECT_ROOT/app/build.gradle"
check_file "$PROJECT_ROOT/tinodesdk/build.gradle"
check_file "$PROJECT_ROOT/settings.gradle"
check_file "$PROJECT_ROOT/codemagic.yaml"
check_file "$PROJECT_ROOT/CODEMAGIC_SETUP.md"

echo -e "\n${YELLOW}Step 3: Checking Gradle Wrapper${NC}"
echo "--------------------------------"

if [ -x "$PROJECT_ROOT/gradlew" ]; then
    echo -e "${GREEN}✓${NC} Gradle wrapper is executable"
else
    echo -e "${RED}✗${NC} Making Gradle wrapper executable..."
    chmod +x "$PROJECT_ROOT/gradlew"
fi

echo -e "\n${YELLOW}Step 4: Configuration Status${NC}"
echo "----------------------------"

if [ -f "$PROJECT_ROOT/keystore.properties" ]; then
    echo -e "${GREEN}✓${NC} keystore.properties found (local builds can use it)"
else
    echo -e "${YELLOW}⚠${NC} keystore.properties not found"
    echo "  This is expected. Configure signing in Codemagic UI instead:"
    echo "  Project Settings → Code signing → Android signing"
fi

if [ -f "$PROJECT_ROOT/app/google-services.json" ]; then
    echo -e "${GREEN}✓${NC} google-services.json found"
else
    echo -e "${YELLOW}⚠${NC} google-services.json not found (Firebase push notifications won't work)"
    echo "  Download from Firebase Console if you need push notifications"
fi

echo -e "\n${YELLOW}Step 5: Suggested Next Steps${NC}"
echo "----------------------------"

echo "1. Create keystore for signing (if not already done):"
echo "   keytool -genkey -v -keystore tindroid.jks -keyalg RSA -keysize 2048 -validity 10000 -alias tindroid"
echo ""
echo "2. Create keystore.properties file:"
echo "   cat > keystore.properties << EOF"
echo "   storeFile=$(pwd)/tindroid.jks"
echo "   storePassword=YOUR_STORE_PASSWORD"
echo "   keyAlias=tindroid"
echo "   keyPassword=YOUR_KEY_PASSWORD"
echo "   EOF"
echo ""
echo "3. Test local build:"
echo "   ./gradlew assembleDebug    # Build debug APK"
echo "   ./gradlew assembleRelease  # Build release APK (requires keystore)"
echo ""
echo "4. Set up Codemagic account:"
echo "   https://codemagic.io"
echo ""
echo "5. Connect your GitHub repository to Codemagic"
echo ""
echo "6. Configure signing in Codemagic UI:"
echo "   Project Settings → Code signing → Android signing"
echo "   Upload your keystore file and set credentials"
echo ""
echo "7. Set environment variables in Codemagic UI:"
echo "   Project Settings → Environment variables"
echo "   - DEVELOPER_EMAIL (optional)"
echo "   - SLACK_CHANNEL_ID (optional)"
echo "   - For Play Store deployment:"
echo "     - GCLOUD_SERVICE_ACCOUNT_CREDENTIALS"
echo ""
echo "8. Enable webhooks for automatic builds"
echo ""

echo -e "\n${YELLOW}Step 6: Verify Build Configuration${NC}"
echo "----------------------------------"

echo "Testing Gradle configuration..."

if "$PROJECT_ROOT/gradlew" -v > /dev/null 2>&1; then
    echo -e "${GREEN}✓${NC} Gradle wrapper is working"
else
    echo -e "${RED}✗${NC} Gradle wrapper failed"
    exit 1
fi

echo -e "\n${GREEN}Setup verification complete!${NC}"
echo ""
echo "For detailed setup instructions, see: CODEMAGIC_SETUP.md"
