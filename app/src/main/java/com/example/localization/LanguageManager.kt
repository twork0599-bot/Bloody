package com.example.localization

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    BANGLA("bn", "Bangla", "বাংলা")
}

object LifeStrings {
    fun get(key: String, lang: AppLanguage): String {
        val entry = translations[key] ?: return key
        return when (lang) {
            AppLanguage.ENGLISH -> entry.first
            AppLanguage.BANGLA -> entry.second
        }
    }

    // Map of key -> Pair(English, Bangla)
    private val translations = mapOf(
        // General Brand & Navigation
        "app_name" to Pair("LIFEDROP", "লাইফড্রপ"),
        "tagline" to Pair("Every Drop Saves a Life.", "প্রতিটি রক্তবিন্দু বাঁচায় একটি জীবন।"),
        "nav_home" to Pair("Home", "হোম"),
        "nav_find_donors" to Pair("Find Donors", "রক্তদাতা খুঁজুন"),
        "nav_emergency" to Pair("Emergency", "জরুরি রক্ত"),
        "nav_messages" to Pair("Messages", "বার্তা"),
        "nav_profile" to Pair("Profile", "প্রোফাইল"),
        "nav_compatibility" to Pair("Compatibility", "রক্তের সামঞ্জস্য"),
        "nav_hospitals" to Pair("Hospitals", "হাসপাতাল"),
        "nav_admin" to Pair("Admin Hub", "অ্যাডমিন হাব"),
        "nav_history" to Pair("Donations", "রক্তদান ইতিহাস"),

        // Onboarding & Language Selector
        "select_language_title" to Pair("Choose Your Language", "আপনার ভাষা নির্বাচন করুন"),
        "select_language_subtitle" to Pair("You can change your language anytime in profile settings.", "আপনি প্রোফাইল সেটিংস থেকে যেকোনো সময় ভাষা পরিবর্তন করতে পারবেন।"),
        "continue_btn" to Pair("Continue", "এগিয়ে যান"),
        "get_started" to Pair("Get Started", "শুরু করুন"),

        // Auth
        "auth_title" to Pair("LIFEDROP Account", "লাইফড্রপ অ্যাকাউন্ট"),
        "auth_subtitle" to Pair("Sign in to connect with donors & save lives", "রক্তদাতাদের সাথে যুক্ত হতে সাইন ইন করুন"),
        "sign_in_google" to Pair("Sign in with Google", "Google দিয়ে সাইন ইন করুন"),
        "or_use_phone_email" to Pair("Or continue with Mobile / Email", "অথবা মোবাইল / ইমেইল দিয়ে এগিয়ে যান"),
        "tab_login" to Pair("Sign In", "লগইন"),
        "tab_register" to Pair("Register", "নিবন্ধন"),
        "phone_number" to Pair("Mobile Number", "মোবাইল নম্বর"),
        "email_address" to Pair("Email Address", "ইমেইল ঠিকানা"),
        "full_name" to Pair("Full Name", "পুরো নাম"),
        "password" to Pair("Password", "পাসওয়ার্ড"),
        "blood_group" to Pair("Blood Group", "রক্তের গ্রুপ"),
        "enter_otp" to Pair("Enter 6-digit OTP", "৬-সংখ্যার ওটিপি লিখুন"),
        "verify_otp" to Pair("Verify & Proceed", "যাচাই করুন ও এগিয়ে যান"),
        "resend_otp" to Pair("Resend OTP", "পুনরায় ওটিপি পাঠান"),
        "forgot_password" to Pair("Forgot Password?", "পাসওয়ার্ড ভুলে গেছেন?"),
        "biometrics_prompt" to Pair("Biometric Authentication", "বায়োমেট্রিক নিরাপত্তা"),
        "biometrics_desc" to Pair("Enable Fingerprint / Face Unlock for fast secure access", "দ্রুত ও নিরাপদ প্রবেশের জন্য বায়োমেট্রিক চালু করুন"),
        "logout" to Pair("Log Out", "লগআউট"),
        "delete_account" to Pair("Delete Account", "অ্যাকাউন্ট মুছুন"),
        "delete_account_confirm" to Pair("Are you sure? All your data will be permanently wiped.", "আপনি কি নিশ্চিত? আপনার সমস্ত তথ্য স্থায়ীভাবে মুছে ফেলা হবে।"),

        // Home Screen
        "hello_donor" to Pair("Hello, Lifesaver!", "স্বাগতম, জীবন রক্ষাকারী!"),
        "your_blood_group" to Pair("Your Blood Group", "আপনার রক্তের গ্রুপ"),
        "donor_status" to Pair("Donor Status", "রক্তদাতার অবস্থা"),
        "status_available" to Pair("Available to Donate", "রক্তদানে প্রস্তুত"),
        "status_unavailable" to Pair("Temporarily Unavailable", "সাময়িক অনুপলব্ধ"),
        "eligible_now" to Pair("Eligible to Donate Now", "এখন রক্তদানের যোগ্য"),
        "days_until_eligible" to Pair("days until next eligible donation", "দিন বাকি পরবর্তী রক্তদানের জন্য"),
        "broadcast_emergency_btn" to Pair("BROADCAST EMERGENCY REQUEST", "জরুরি রক্তের অনুরোধ পাঠান"),
        "urgent_requests_near_you" to Pair("Active Emergency Requests", "সক্রিয় জরুরি অনুরোধসমূহ"),
        "smart_matched_donors" to Pair("Smart Matched Donors Near You", "কাছের উপযুক্ত রক্তদাতাগণ"),
        "quick_stats_title" to Pair("Platform Impact", "প্ল্যাটফর্ম পরিসংখ্যান"),
        "stat_total_donations" to Pair("Verified Donations", "যাচাইকৃত রক্তদান"),
        "stat_lives_saved" to Pair("Estimated Lives Saved", "আনুমানিক জীবন রক্ষা"),
        "stat_active_donors" to Pair("Active Donors", "সক্রিয় রক্তদাতা"),
        "stat_hospitals" to Pair("Partner Hospitals", "পার্টনার হাসপাতাল"),
        "medical_safety_banner" to Pair("Important: Transfusions strictly require hospital laboratory testing & crossmatching.", "গুরুত্বপূর্ণ: রক্ত সঞ্চালনের পূর্বে হাসপাতালের ল্যাব টেস্ট এবং ক্রস-ম্যাচিং বাধ্যতামূলক।"),

        // Find Donors / Search
        "search_donors_title" to Pair("Find Compatible Donors", "সামঞ্জস্যপূর্ণ রক্তদাতা খুঁজুন"),
        "filter_all" to Pair("All", "সব"),
        "filter_distance" to Pair("Radius", "দূরত্ব"),
        "filter_component" to Pair("Component", "রক্তের উপাদান"),
        "filter_verified_only" to Pair("Verified Only", "শুধু যাচাইকৃত"),
        "filter_available_only" to Pair("Available Now", "এখন উপলব্ধ"),
        "view_radar" to Pair("Radar Map", "রাডার ম্যাপ"),
        "view_list" to Pair("Donor List", "তালিকা"),
        "contact_donor" to Pair("Message Donor", "বার্তা পাঠান"),
        "call_donor" to Pair("Call Donor", "কল করুন"),
        "verified_badge" to Pair("Verified Donor", "যাচাইকৃত রক্তদাতা"),
        "distance_away" to Pair("km away", "কিমি দূরে"),
        "last_donated" to Pair("Last Donated", "সর্বশেষ রক্তদান"),
        "total_donations" to Pair("Donations", "রক্তদান"),

        // Emergency Broadcast
        "emergency_title" to Pair("Emergency Blood Broadcast", "জরুরি রক্ত সম্প্রচার"),
        "patient_name" to Pair("Patient Name", "রোগীর নাম"),
        "required_units" to Pair("Units (Bags) Needed", "প্রয়োজনীয় ব্যাগ/ইউনিট"),
        "blood_component" to Pair("Component Type", "উপাদানের ধরন"),
        "comp_whole_blood" to Pair("Whole Blood", "সম্পূর্ণ রক্ত"),
        "comp_rbc" to Pair("Red Blood Cells (RBC)", "লোহিত রক্তকণিকা (RBC)"),
        "comp_platelets" to Pair("Platelets", "প্লাটিলেট"),
        "comp_plasma" to Pair("Plasma", "প্লাজমা"),
        "hospital_name" to Pair("Hospital / Clinic Name", "হাসপাতাল / ক্লিনিকের নাম"),
        "hospital_address" to Pair("Hospital Address / Location", "হাসপাতালের ঠিকানা / অবস্থান"),
        "district" to Pair("District", "জেলা"),
        "upazila" to Pair("Upazila / Area", "উপজেলা / এলাকা"),
        "urgency_level" to Pair("Urgency Level", "জরুরিত্বের মাত্রা"),
        "urgency_critical" to Pair("CRITICAL (Within 2 Hours)", "সংকটাপন্ন (২ ঘণ্টার মধ্যে)"),
        "urgency_urgent" to Pair("URGENT (Today)", "জরুরি (আজকের মধ্যে)"),
        "urgency_normal" to Pair("NORMAL (Within 24-48 Hours)", "স্বাভাবিক (২৪-৪৮ ঘণ্টার মধ্যে)"),
        "broadcast_radius" to Pair("Broadcast Radius", "সম্প্রচারের পরিধি"),
        "contact_phone" to Pair("Attendant Contact Phone", "যোগাযোগের ফোন নম্বর"),
        "emergency_notes" to Pair("Clinical Notes / Instructions", "বিশেষ নির্দেশনা / নোট"),
        "submit_broadcast" to Pair("Launch Live Broadcast", "জরুরি সম্প্রচার শুরু করুন"),
        "broadcast_active" to Pair("Emergency Broadcast Active", "জরুরি সম্প্রচার চলছে"),
        "matching_donors_notified" to Pair("donors alerted in your area", "জন রক্তদাতাকে নোটিফিকেশন পাঠানো হয়েছে"),
        "cancel_request" to Pair("Cancel Request", "অনুরোধ বাতিল করুন"),
        "mark_fulfilled" to Pair("Mark as Fulfilled", "সম্পন্ন হিসেবে চিহ্নিত করুন"),
        "view_responses" to Pair("Donor Responses", "রক্তদাতাদের সাড়া"),

        // Donor Safety & Eligibility
        "safety_guide_title" to Pair("Donor Eligibility & Safety", "রক্তদাতার যোগ্যতা ও নিরাপত্তা"),
        "rule_age" to Pair("Age between 18-65 years", "বয়স ১৮ থেকে ৬৫ বছরের মধ্যে"),
        "rule_weight" to Pair("Minimum body weight of 50 kg", "ন্যূনতম ওজন ৫০ কেজি"),
        "rule_interval" to Pair("Minimum 90 days interval between donations", "দুই রক্তদানের মাঝে ন্যূনতম ৯০ দিনের ব্যবধান"),
        "rule_hemoglobin" to Pair("Healthy hemoglobin levels (>=12.5 g/dL)", "হিমোগ্লোবিনের মাত্রা স্বাভাবিক (ন্যূনতম ১২.৫)"),
        "rule_health" to Pair("Free from cold, fever, recent antibiotics or surgery", "জ্বর, সর্দি, সাম্প্রতিক অস্ত্রোপচার বা অ্যান্টিবায়োটিক মুক্ত"),
        "self_declaration" to Pair("Donor Safety Self-Declaration", "রক্তদাতার স্বাস্থ্য স্বঘোষণা"),
        "i_confirm_eligibility" to Pair("I confirm I meet standard health safety guidelines", "আমি নিশ্চিত করছি যে আমি স্বাস্থ্য সুরক্ষা নির্দেশিকা মেনে চলছি"),

        // Medical Compatibility Reference
        "compat_title" to Pair("Blood Compatibility Matrix", "রক্তের গ্রুপ সামঞ্জস্য নির্দেশিকা"),
        "can_donate_to" to Pair("Can Donate To", "কাদের রক্ত দিতে পারবেন"),
        "can_receive_from" to Pair("Can Receive From", "কাদের থেকে রক্ত নিতে পারবেন"),
        "universal_rbc_donor" to Pair("O- is the Universal Red Blood Cell Donor", "O- হলো সার্বজনীন লোহিত রক্তকণিকা দাতা"),
        "universal_rbc_recipient" to Pair("AB+ is the Universal Red Blood Cell Recipient", "AB+ হলো সার্বজনীন লোহিত রক্তকণিকা গ্রহীতা"),
        "universal_plasma_donor" to Pair("AB is the Universal Plasma Donor", "AB হলো সার্বজনীন প্লাজমা দাতা"),
        "universal_plasma_recipient" to Pair("O is the Universal Plasma Recipient", "O হলো সার্বজনীন প্লাজমা গ্রহীতা"),
        "transfusion_disclaimer" to Pair("Clinical Notice: Emergency compatibility tables are for education only. In all real transfusions, blood crossmatching and pathogen screening must be carried out in an accredited blood bank laboratory under medical supervision.", "চিকিৎসা সংক্রান্ত সতর্কতা: এই নির্দেশিকা শুধুমাত্র সাধারণ তথ্যের জন্য। বাস্তব ক্ষেত্রে যেকোনো রক্ত সঞ্চালনের পূর্বে অনুমোদিত ব্লাড ব্যাংকে বাধ্যতামূলক ক্রস-ম্যাচিং এবং ল্যাব টেস্ট করতে হবে।"),

        // Chat & Communication
        "chat_title" to Pair("Direct Lifedrop Chat", "লাইফড্রপ চ্যাট"),
        "chat_placeholder" to Pair("Type a secure message...", "একটি বার্তা লিখুন..."),
        "quick_reply_on_my_way" to Pair("I am on my way to the hospital", "আমি হাসপাতালের দিকে রওনা হয়েছি"),
        "quick_reply_reached" to Pair("I have arrived at the blood bank", "আমি ব্লাড ব্যাংকে পৌঁছেছি"),
        "quick_reply_call_me" to Pair("Please call me directly", "দয়া করে আমাকে সরাসরি কল করুন"),
        "read_receipt" to Pair("Read", "পঠিত"),
        "delivered_receipt" to Pair("Delivered", "পৌঁছেছে"),

        // Donation History & Certificates
        "history_title" to Pair("My Donation Journey", "আমার রক্তদানের বিবরণ"),
        "certificate_title" to Pair("Certificate of Appreciation", "সম্মাননা সনদপত্র"),
        "certificate_subtitle" to Pair("In recognition of heroic service to humanity", "মানবতার সেবায় অনন্য অবদানের স্বীকৃতিস্বরূপ"),
        "certificate_presented_to" to Pair("This certificate is proudly awarded to", "এই প্রশংসাপত্রটি আনন্দের সাথে প্রদান করা হলো"),
        "certificate_verified_by" to Pair("Verified by Partner Hospital", "পার্টনার হাসপাতাল কর্তৃক অনুমোদিত"),
        "view_certificate" to Pair("View Digital Certificate", "ডিজিটাল সনদপত্র দেখুন"),
        "download_certificate" to Pair("Save Certificate", "সনদপত্র সংরক্ষণ করুন"),
        "badges_title" to Pair("Lifedrop Achievements", "অর্জনসমূহ ও ব্যাজ"),
        "badge_first_donation" to Pair("First Drop (1 Donation)", "প্রথম রক্তদান"),
        "badge_helping_hand" to Pair("Helping Hand (3 Donations)", "সহায়তাকারী (৩টি রক্তদান)"),
        "badge_life_saver" to Pair("Life Saver (5 Donations)", "জীবন রক্ষাকারী (৫টি রক্তদান)"),
        "badge_community_hero" to Pair("Community Hero (10+ Donations)", "কমিউনিটি হিরো (১০+ রক্তদান)"),

        // Hospitals & Partners
        "hospitals_title" to Pair("Verified Hospitals & Blood Banks", "যাচাইকৃত হাসপাতাল ও ব্লাড ব্যাংক"),
        "units_available" to Pair("Available Stock", "মজুত রক্ত"),
        "hospital_directions" to Pair("Get Directions", "দিকনির্দেশনা"),
        "hospital_call" to Pair("Emergency Desk", "জরুরি ডেস্কে কল"),
        "partner_verified_badge" to Pair("Verified Healthcare Partner", "যাচাইকৃত স্বাস্থ্যসেবা পার্টনার"),

        // Trust & Fraud Protection
        "report_request" to Pair("Report Suspicious Request", "সন্দেহজনক অনুরোধ রিপোর্ট করুন"),
        "report_reason" to Pair("Reason for Report", "রিপোর্টের কারণ"),
        "report_fake" to Pair("Suspected Fake Blood Request", "ভুয়া রক্তের অনুরোধ সন্দেহ"),
        "report_money" to Pair("Demanding Money / Extortion", "অর্থ দাবি বা চাঁদাবাজি"),
        "report_spam" to Pair("Spam or Duplicate Post", "স্প্যাম বা একাধিক পোস্ট"),
        "report_submitted" to Pair("Report Submitted. Our safety team will review immediately.", "রিপোর্ট জমা হয়েছে। আমাদের নিরাপত্তা দল অবিলম্বে যাচাই করবে।"),
        "fraud_risk_score" to Pair("Trust & Fraud Score", "নিরাপত্তা ও বিশ্বাসযোগ্যতা স্কোর"),
        "score_safe" to Pair("Verified Safe", "নিরাপদ ও নির্ভরযোগ্য"),
        "score_caution" to Pair("Under Review", "পর্যালোচনাধীন"),

        // Admin & Partner Verification Hub
        "admin_hub_title" to Pair("Admin & Partner Console", "অ্যাডমিন ও পার্টনার কনসোল"),
        "tab_overview" to Pair("Overview", "সারসংক্ষেপ"),
        "tab_verify_donations" to Pair("Verify Donations", "রক্তদান যাচাই"),
        "tab_fraud_reports" to Pair("Fraud Queue", "রিপোর্ট কিউ"),
        "tab_announcements" to Pair("Announcements", "বিজ্ঞপ্তি"),
        "verify_donation_btn" to Pair("Confirm & Issue Certificate", "যাচাই করুন ও সনদ দিন"),
        "broadcast_announcement" to Pair("Send System Announcement", "সিস্টেম বিজ্ঞপ্তি প্রচার করুন"),

        // Profile & Settings
        "profile_title" to Pair("Donor Profile", "রক্তদাতার প্রোফাইল"),
        "edit_profile" to Pair("Edit Profile", "প্রোফাইল সম্পাদনা"),
        "save_changes" to Pair("Save Changes", "সংরক্ষণ করুন"),
        "privacy_settings" to Pair("Privacy & Security", "গোপনীয়তা ও নিরাপত্তা"),
        "hide_phone_toggle" to Pair("Hide Phone Number from Public", "মোবাইল নম্বর গোপন রাখুন"),
        "approximate_location_toggle" to Pair("Show Approximate Location Only", "শুধুমাত্র আনুমানিক এলাকা প্রদর্শন করুন"),
        "app_language_setting" to Pair("App Language", "অ্যাপের ভাষা"),
        "dark_mode_setting" to Pair("Dark Theme", "ডার্ক মোড"),
        "notification_settings" to Pair("Notification Preferences", "নোটিফিকেশন পছন্দসমূহ"),
        "terms_privacy" to Pair("Terms & Privacy Policy", "ব্যবহারের শর্তাবলী ও গোপনীয়তা নীতি"),
        "help_support" to Pair("Help & Emergency Support", "সহায়তা ও জরুরি হেল্পলাইন")
    )
}
