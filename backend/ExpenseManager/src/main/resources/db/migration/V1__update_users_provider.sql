-- Add provider column as nullable first
ALTER TABLE IF EXISTS users 
ADD COLUMN IF NOT EXISTS provider varchar(255);

-- Update existing records to have 'LOCAL' as provider
UPDATE users 
SET provider = 'LOCAL' 
WHERE provider IS NULL;

-- Now make the column non-null and add the check constraint
ALTER TABLE users 
ALTER COLUMN provider SET NOT NULL,
ADD CONSTRAINT provider_check CHECK (provider IN ('LOCAL', 'GOOGLE', 'GITHUB')); 