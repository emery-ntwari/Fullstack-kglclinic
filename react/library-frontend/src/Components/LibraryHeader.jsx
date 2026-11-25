import { Link } from 'react-router'

function LibraryHeader() {
  return (
    <header className="library-header">
      <h1>Library Management System</h1>
      <p>This is a Library System that will help you to manage the books</p>
      <nav className="library-navigation" aria-label="Main navigation">
        <Link to="/">Home</Link>
        <Link to="/books">Books</Link>
        <Link to="/office">Offices</Link>
      </nav>
    </header>
  )
}

export default LibraryHeader
