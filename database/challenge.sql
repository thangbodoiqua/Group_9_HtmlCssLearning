INSERT INTO CHALLENGE_TYPE(Challenge_type) 
VALUES("CSS_DEBUG"), ("PIXEL_PERFECT");

INSERT INTO challenge (challenge_title, challenge_difficulty, challenge_hints, challenge_instructions, challenge_type)
VALUES 
('Flexbox-Centering-Fix', 'Easy', 
 'Check the height of the container and use align-items: center.', 
 'The item is only centered horizontally. Fix the CSS so it is perfectly centered both vertically and horizontally within the 100vh container.', 
 'CSS_DEBUG'),

('Sticky-Header-Overlap', 'Medium', 
 'Use z-index to bring the header to the front and add padding-top to the content.', 
 'The header is sticky but it stays behind the content when scrolling. Fix the layering issue and ensure the content is not hidden behind the header.', 
 'CSS_DEBUG'),

('Grid-Responsive-Overflow', 'Hard', 
 'Use grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)) instead of fixed columns.', 
 'The grid layout breaks on small screens and causes horizontal scroll. Make the grid responsive so it adjusts the number of columns based on screen width.', 
 'CSS_DEBUG');

INSERT INTO challenge (challenge_title, challenge_difficulty, challenge_hints, challenge_instructions, challenge_type)
VALUES 
('Modern-Glassmorphism-Card', 'Medium', 
 'Use backdrop-filter: blur() and a semi-transparent white background.', 
 'Reproduce the Glassmorphism effect exactly as shown. Focus on the blur effect, border-radius, and the subtle white border.', 
 'PIXEL_PERFECT'),

('Neumorphic-Soft-Button', 'Easy', 
 'Use two box-shadows: one light (top-left) and one dark (bottom-right).', 
 'Create a Neumorphic "soft" button. The button should look like it is pushed out from the background using shadows.', 
 'PIXEL_PERFECT'),

('Responsive-Navigation-Menu', 'Hard', 
 'Use a media query for max-width: 768px and change flex-direction to column.', 
 'The desktop menu looks good, but it must transform into a vertical mobile menu on smaller screens. Match the mobile design exactly.', 
 'PIXEL_PERFECT');
