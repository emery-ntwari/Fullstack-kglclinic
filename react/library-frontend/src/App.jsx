import { Route, Routes } from 'react-router'
import './App.css'
import Office from './Components/Office'
import LibraryLogin from './Components/LibraryLogin'
import LibraryBook from './Components/LibraryBook'
import LibraryHeader from './Components/LibraryHeader'

function App() {
  return (
    <>
      <LibraryHeader />

      <Routes>
        <Route path="/" element={<LibraryLogin />} />
        <Route path="/office" element={<Office />} />
        <Route path="/books" element={<LibraryBook />} />
        <Route path="*" element={<LibraryLogin />} />
      </Routes>
    </>
  )
}

export default App
