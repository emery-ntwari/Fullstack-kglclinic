import React, { useState } from 'react'
import LibraryCards from './LibraryCards';

function LibraryBook() {
    const books = [
        {
            code:"WEB 001",
            name:"Software Engineering",
            price: 1000
        },
        {
            code:"WEB 002",
            name:"Web Development",
            price: 2000
        },
        {
            code:"WEB 003",
            name:"Database Management System",
            price: 3000
        }
    ]
    const totalPrice = books.reduce((total, book) => total + book.price, 0)

    function LibraryBooks() {
        const [books, setBooks] = useState([books]);

        function CaptureBooks() {
            setBooks[books];
        }
    }
  return (
    <div>
     <h2>List of Books</h2>
      <ul>
       {
        books.map(book => <LibraryCards bookInfo={book} />)
       }
      </ul>
      <p>The Total price for {books.length} books is ${totalPrice}</p>
    </div>
  )
}

export default LibraryBook
