# Push Changes to GitHub — Quick Guide

## الطريقة الأسهل (Run in your terminal):

```bash
cd /home/omar/Documents/jarvis
./PUSH_CHANGES.sh
```

ستُطلب منك كلمة السر أو GitHub Personal Access Token إذا لزم الأمر.

---

## أو يدويًا (Manual alternative):

```bash
cd /home/omar/Documents/jarvis
git status
git push -u origin main
```

---

## بعد الدفع:

1. اذهب إلى: https://github.com/slto7-code-fixer/jarvis
2. انقر على تبويب **Actions**
3. ستري الـ workflows:
   - `validate-ci.yml` — يفحص الملفات والإعدادات (سريع)
   - `android-build.yml` — يبني الـ APK (قد يستغرق 10-15 دقيقة)
4. بعد انتهاء البناء، انقر على الـ workflow وتنزيل artifact `app-debug-apk` (ملف APK)

---

## إذا واجهت مشكلة "Permission denied":

### الحل 1: استخدم Personal Access Token
1. اذهب إلى GitHub Settings → Developer settings → Personal access tokens
2. انقر **Generate new token** واختر الأذونات: `repo` و `workflow`
3. انسخ التوكن
4. عند طلب كلمة السر: اترك username فارغًا وألصق التوكن كـ password

### الحل 2: استخدم SSH (أفضل على المدى الطويل)
```bash
# إنشاء مفتاح SSH (إن لم تنشئه)
ssh-keygen -t ed25519 -C "your_email@example.com"

# انسخ المفتاح العام
cat ~/.ssh/id_ed25519.pub

# ثم:
# 1. اذهب إلى GitHub > Settings > SSH and GPG keys
# 2. انقر Add SSH key وألصق المفتاح

# ثم غيّر remote إلى SSH
git remote set-url origin git@github.com:slto7-code-fixer/jarvis.git

# ثم ادفع
git push -u origin main
```

---

## أسئلة شائعة:

**س: لماذا يطلب كلمة السر؟**
ج: GitHub لا يقبل كلمات مرور HTTPS مباشرة — استخدم Personal Access Token أو SSH.

**س: هل آمن أن أدخل بيانات اعتماد في الطرفية؟**
ج: نعم، Git يخزن البيانات بأمان بعد المرة الأولى (credential helper). لكن SSH أكثر أماناً على المدى الطويل.

**س: كم من الوقت يستغرق البناء؟**
ج: `validate-ci` ~ 2-3 دقائق
   `android-build` ~ 10-15 دقيقة (أول مرة، أسرع لاحقاً لأن الـ cache)

**س: هل يمكنني رؤية سجل البناء؟**
ج: نعم، في صفحة Actions على GitHub.

---

تم! الآن شغّل السكربت من جهازك وأخبرني عندما ينتهي الدفع بنجاح.
