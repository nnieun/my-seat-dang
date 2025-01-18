-- 조회쿼리성능개선을 위한 더미데이터 생성 쿼리


--Customer


-- 재귀 깊이 설정 (기본값은 1000이므로, 더 많은 데이터를 생성하려면 증가시켜야 합니다)
SET SESSION cte_max_recursion_depth = 1000000;

INSERT INTO tbl_member (
    member_email, member_name, join_date, member_password, member_phone, member_role, member_status, DTYPE, customer_nick_name, customer_gender, customer_birthday, image_gen_left, customer_profile_image
)
-- Customer 더미 데이터 삽입
WITH RECURSIVE cte AS (
    SELECT 1 AS id
    UNION ALL
    SELECT id + 1 FROM cte WHERE id < 1000000
)
SELECT
    CONCAT('customer', id, '@example.com') AS memberEmail,
    CONCAT('Customer', id) AS memberName,
    CURDATE() AS joinDate,
    '1234k!' AS memberPassword, -- 실제로는 암호화된 비밀번호를 사용해야 합니다
    CONCAT('010-', LPAD(FLOOR(RAND() * 10000), 4, '0'), '-', LPAD(FLOOR(RAND() * 10000), 4, '0')) AS memberPhone,
    'ROLE_CUSTOMER' AS memberRole,
    'APPROVED' AS memberStatus,
    'CUSTOMER' AS DTYPE,
    CONCAT('Nick', id) AS customerNickName,
    IF(id % 2 = 0, 'MALE', 'FEMALE') AS customerGender,
    DATE_SUB(CURDATE(), INTERVAL FLOOR(RAND() * 10000) DAY) AS customerBirthday,
    FLOOR(RAND() * 10) AS imageGenLeft,
    CONCAT('https://example.com/profiles/customer', id, '.png') AS customerProfileImage
FROM cte;

-- StoreOwner

-- StoreOwner 더미 데이터 삽입
INSERT INTO tbl_member (
    memberEmail, memberName, joinDate, memberPassword, memberPhone, memberRole, memberStatus, DTYPE, businessLicenseImage, businessLicense, bankAccountCopy, bankAccount, storeOwnerProfileImage
)
WITH RECURSIVE cte AS (
    SELECT 1 AS id
    UNION ALL
    SELECT id + 1 FROM cte WHERE id < 10000
)
SELECT
    CONCAT('storeowner', id, '@example.com') AS memberEmail,
    CONCAT('StoreOwner', id) AS memberName,
    CURDATE() AS joinDate,
    '1234k!' AS memberPassword, -- 실제로는 암호화된 비밀번호를 사용해야 합니다
    CONCAT('010-', LPAD(FLOOR(RAND() * 10000), 4, '0'), '-', LPAD(FLOOR(RAND() * 10000), 4, '0')) AS memberPhone,
    'ROLE_STORE_OWNER' AS memberRole,
    'PENDING_APPROVAL' AS memberStatus,
    'STORE_OWNER' AS DTYPE,
    CONCAT('https://example.com/licenses/license', id, '.png') AS businessLicenseImage,
    CONCAT(LPAD(FLOOR(RAND() * 1000000000), 10, '0')) AS businessLicense,
    CONCAT('https://example.com/bankaccounts/account', id, '.png') AS bankAccountCopy,
    CONCAT('110-', LPAD(FLOOR(RAND() * 1000000), 6, '0'), '-', LPAD(FLOOR(RAND() * 100000), 5, '0')) AS bankAccount,
    CONCAT('https://example.com/profiles/storeowner', id, '.png') AS storeOwnerProfileImage
FROM cte;

-- Admin 더미 데이터 삽입

INSERT INTO tbl_member (
    memberEmail, memberName, joinDate, memberPassword, memberPhone, memberRole, memberStatus, DTYPE
)
WITH RECURSIVE cte AS (
    SELECT 1 AS id
    UNION ALL
    SELECT id + 1 FROM cte WHERE id < 100
)
SELECT
    CONCAT('admin', id, '@example.com') AS memberEmail,
    CONCAT('Admin', id) AS memberName,
    CURDATE() AS joinDate,
    '1234k!' AS memberPassword, -- 실제로는 암호화된 비밀번호를 사용해야 합니다
    CONCAT('010-', LPAD(FLOOR(RAND() * 10000), 4, '0'), '-', LPAD(FLOOR(RAND() * 10000), 4, '0')) AS memberPhone,
    'ROLE_ADMIN' AS memberRole,
    'APPROVED' AS memberStatus,
    'ADMIN' AS DTYPE
FROM cte;
