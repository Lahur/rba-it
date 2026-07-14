import { useState } from 'react'
import type { FormEvent } from 'react'

interface OibSearchFormProps {
    onSearch: (oib: string) => void
    submitLabel?: string
}

function OibSearchForm({ onSearch, submitLabel = 'Search' }: OibSearchFormProps) {
    const [oib, setOib] = useState('')

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        onSearch(oib)
    }

    return <div className="max-w-sm mx-auto p-6 bg-neutral-primary-soft border border-default rounded-base shadow-xs">
        <form onSubmit={handleSubmit}>
            <div className="mb-5">
                <label htmlFor="oib" className="block mb-2.5 text-sm font-medium text-heading">OIB</label>
                <input type="text" id="oib" value={oib}
                       onChange={(e) => setOib(e.target.value)}
                       className="bg-neutral-secondary-medium border border-default-medium text-heading text-sm rounded-base focus:ring-brand focus:border-brand block w-full px-3 py-2.5 shadow-xs placeholder:text-body"
                       placeholder="12345678901" required/>
            </div>
            <button type="submit"
                    className="text-white bg-brand box-border border border-transparent hover:bg-brand-strong focus:ring-4 focus:ring-brand-medium shadow-xs font-medium leading-5 rounded-base text-sm px-4 py-2.5 focus:outline-none">{submitLabel}
            </button>
        </form>
    </div>
}

export default OibSearchForm
