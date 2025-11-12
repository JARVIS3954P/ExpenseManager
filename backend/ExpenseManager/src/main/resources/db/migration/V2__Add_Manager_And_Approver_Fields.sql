-- Add the manager_id column to the _user table to create the self-referencing relationship
ALTER TABLE "user" ADD COLUMN manager_id BIGINT;

-- Add a foreign key constraint for manager_id referencing the user's own id
ALTER TABLE "user" ADD CONSTRAINT fk_user_manager FOREIGN KEY (manager_id) REFERENCES "user"(id);

-- Add the current_approver_id column to the expense table
ALTER TABLE expense ADD COLUMN current_approver_id BIGINT;

-- Add a foreign key constraint for current_approver_id referencing the user id
ALTER TABLE expense ADD CONSTRAINT fk_expense_current_approver FOREIGN KEY (current_approver_id) REFERENCES "user"(id);