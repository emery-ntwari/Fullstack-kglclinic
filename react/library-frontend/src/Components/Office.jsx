import { useEffect, useMemo, useState } from 'react'
import LibraryFooter from './LibraryFooter'

const API_URL = 'http://localhost:8080/api/offices'

async function getErrorMessage(response) {
  const text = await response.text()
  if (!text) {
    return `Request failed (${response.status})`
  }

  try {
    const body = JSON.parse(text)
    return body.message || body.detail || text
  } catch {
    return text
  }
}

function Office() {
  const [offices, setOffices] = useState([])
  const [search, setSearch] = useState('')
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState('')
  const [deletingOfficeNumbers, setDeletingOfficeNumbers] = useState(new Set())

  useEffect(() => {
    const controller = new AbortController()

    async function loadOffices() {
      try {
        const response = await fetch(`${API_URL}/free`, {
          signal: controller.signal,
        })

        if (!response.ok) {
          throw new Error(await getErrorMessage(response))
        }

        const result = await response.json()
        if (!Array.isArray(result)) {
          throw new Error('The office list response was not a list')
        }

        setOffices(result)
      } catch (loadError) {
        if (loadError.name !== 'AbortError') {
          setError(`Could not load offices: ${loadError.message}`)
        }
      } finally {
        if (!controller.signal.aborted) {
          setIsLoading(false)
        }
      }
    }

    loadOffices()
    return () => controller.abort()
  }, [])

  const filteredOffices = useMemo(() => {
    const query = search.trim().toLocaleLowerCase()
    if (!query) {
      return offices
    }

    return offices.filter((office) =>
      `${office.officeName ?? ''} ${office.officeNumber ?? ''}`
        .toLocaleLowerCase()
        .includes(query),
    )
  }, [offices, search])

  async function deleteOffice(officeNumber) {
    setError('')
    setDeletingOfficeNumbers((current) => new Set(current).add(officeNumber))

    try {
      const response = await fetch(
        `${API_URL}/delete-by-number/${encodeURIComponent(officeNumber)}`,
        { method: 'DELETE' },
      )

      if (!response.ok) {
        throw new Error(await getErrorMessage(response))
      }

      setOffices((current) =>
        current.filter((office) => office.officeNumber !== officeNumber),
      )
    } catch (deleteError) {
      setError(`Could not delete office ${officeNumber}: ${deleteError.message}`)
    } finally {
      setDeletingOfficeNumbers((current) => {
        const next = new Set(current)
        next.delete(officeNumber)
        return next
      })
    }
  }

  return (
    <main className="office-page">
      <section className="office-section" aria-labelledby="office-page-title">
        <h1 id="office-page-title">Offices Page</h1>

        <div className="office-search">
          <label htmlFor="office-search">Search:</label>
          <input
            id="office-search"
            type="search"
            placeholder="Office name or number"
            value={search}
            onChange={(event) => setSearch(event.target.value)}
          />
        </div>

        {error && (
          <p className="office-message office-error" role="alert">
            {error}
          </p>
        )}

        <div className="office-table-container">
          <table className="office-table">
            <thead>
              <tr>
                <th scope="col">id</th>
                <th scope="col">officeNumber</th>
                <th scope="col">officeName</th>
                <th scope="col">Action</th>
              </tr>
            </thead>
            <tbody>
              {isLoading ? (
                <tr>
                  <td colSpan="4">Loading offices...</td>
                </tr>
              ) : filteredOffices.length > 0 ? (
                filteredOffices.map((office) => (
                  <tr key={office.id}>
                    <td>{office.id}</td>
                    <td>{office.officeNumber}</td>
                    <td>{office.officeName}</td>
                    <td className="office-actions">
                      <button
                        type="button"
                        onClick={() => deleteOffice(office.officeNumber)}
                        disabled={deletingOfficeNumbers.has(office.officeNumber)}
                      >
                        {deletingOfficeNumbers.has(office.officeNumber)
                          ? 'Deleting...'
                          : 'Delete'}
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="4">
                    {search ? 'No matching offices found.' : 'No offices available.'}
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </section>

      <LibraryFooter />
    </main>
  )
}

export default Office
