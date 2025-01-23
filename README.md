# Bookstore Inventory Management Android App
Android app with Room DB, Query, and Recycler View. It contains CRUD operations, input validation, and sorting to manage inventory.

## Implemented basic CRUD operation
### 1. Add a New Book
- User can input details for a new book (title, author, price, quantity) after clicking the plus sign ('+') on the support bar
- System saves the new book to the database
- New book appears in the list of books
### 2. View All Books
- User can see a list of all books in the inventory
- List displays key information for each book (title, author, price, quantity)
- List is scrollable if there are many books
### 3. Update Book Details
- User can select a book to edit its details by clicking the edit button next to it
- System allows modification of book information
- Updated information is saved and reflected in the list
### 4. Delete a Book
- User can remove a book from the inventory by clicking the delete button in the row
- System deletes the book from the database
- Book is removed from the displayed list
### 5. View Book Details
- User can tap on a book to view its full details by clicking anywhere in the row
- System displays a detailed view of the selected book
- The auto-generated id will also be included
### 6. Data Persistence
- All data is persistently stored using Room database
- Data remains available across app restarts
## Additional features
### 1. Sort books by quantity in ascending order
- User can view all books according to the ascending order of quantities by clicking 'Sort by quantity (asc)' in the support bar menu
- It may help to provide a simple analysis on low inventory books
### 2. Sort books by quantity in descending order
- User can view all books according to the descending order of quantities by clicking 'Sort by quantity (desc)' in the support bar menu
### 3. Reset sorting criteria
- User can reset books to default order in to the Room database
### 4. Refresh book list
- User can refresh the book list to newest data
- The sorting criteria will be unchanged
### 5. Input validation checks
- Set the price edit text with input type of 'numberDecimal' to prevent negative numbers and non-number inputs
- Set the quantity edit text with input type of 'number' that allow non-negative integers only
- Blank checks on all input fields
### 6. Primary key 'id'
- Set an auto-generated primary key id to 'BookEntity', so that each records can be recognized, and key information for each book (title, author, price, quantity) can all be updated
## Future improvements
- Potential modifications on input validations, some additional checks may be added
- Adding more new features regarding sorting and searching
- Authentication and authorization
- Fixing any undiscovered bugs
