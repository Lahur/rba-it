import { useEffect, useState } from 'react'
import type { FormEvent } from 'react'
import { useAppDispatch, useAppSelector } from '../app/hooks'
import { clearSelected, createCardRequest } from '../stores/cardRequestSlice'
import { CardStatus } from '../models/CardStatus'
import CardRequestDetailsCard from '../components/CardRequestDetailsCard'

function RequestFormCard() {
    const dispatch = useAppDispatch()
    const [firstName, setFirstName] = useState('')
    const [lastName, setLastName] = useState('')
    const [oib, setOib] = useState('')
    const [status, setStatus] = useState<CardStatus>(CardStatus.PENDING)

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        dispatch(createCardRequest({ firstName, lastName, oib, status }))
    }

    return <div className="max-w-sm mx-auto p-6 bg-neutral-primary-soft border border-default rounded-base shadow-xs">
        <form onSubmit={handleSubmit}>
            <div className="mb-5">
                <label htmlFor="firstName" className="block mb-2.5 text-sm font-medium text-heading">First name</label>
                <input type="text" id="firstName" value={firstName}
                       onChange={(e) => setFirstName(e.target.value)}
                       className="bg-neutral-secondary-medium border border-default-medium text-heading text-sm rounded-base focus:ring-brand focus:border-brand block w-full px-3 py-2.5 shadow-xs placeholder:text-body"
                       placeholder="John" required/>
            </div>
            <div className="mb-5">
                <label htmlFor="lastName" className="block mb-2.5 text-sm font-medium text-heading">Last name</label>
                <input type="text" id="lastName" value={lastName}
                       onChange={(e) => setLastName(e.target.value)}
                       className="bg-neutral-secondary-medium border border-default-medium text-heading text-sm rounded-base focus:ring-brand focus:border-brand block w-full px-3 py-2.5 shadow-xs placeholder:text-body"
                       placeholder="Doe" required/>
            </div>
            <div className="mb-5">
                <label htmlFor="oib" className="block mb-2.5 text-sm font-medium text-heading">OIB</label>
                <input type="text" id="oib" value={oib}
                       onChange={(e) => setOib(e.target.value)}
                       className="bg-neutral-secondary-medium border border-default-medium text-heading text-sm rounded-base focus:ring-brand focus:border-brand block w-full px-3 py-2.5 shadow-xs placeholder:text-body"
                       placeholder="12345678901" required/>
            </div>
            <div className="mb-5">
                <label htmlFor="status" className="block mb-2.5 text-sm font-medium text-heading">Status</label>
                <select id="status" value={status}
                        onChange={(e) => setStatus(e.target.value as CardStatus)}
                        className="bg-neutral-secondary-medium border border-default-medium text-heading text-sm rounded-base focus:ring-brand focus:border-brand block w-full px-3 py-2.5 shadow-xs">
                    {Object.values(CardStatus).map((value) => (
                        <option key={value} value={value}>{value}</option>
                    ))}
                </select>
            </div>
            <button type="submit"
                    className="text-white bg-brand box-border border border-transparent hover:bg-brand-strong focus:ring-4 focus:ring-brand-medium shadow-xs font-medium leading-5 rounded-base text-sm px-4 py-2.5 focus:outline-none">Submit
            </button>
        </form>
    </div>
}

function RequestResultCard() {
    const selected = useAppSelector((state) => state.cardRequest.selected)

    if (!selected) {
        return null
    }

    return <CardRequestDetailsCard cardRequest={selected} title="Card request created"/>
}

function CardRequestFormPage() {
    const dispatch = useAppDispatch()

    useEffect(() => {
        dispatch(clearSelected())
    }, [dispatch])

    return <>
        <RequestFormCard/>
        <RequestResultCard/>
    </>
}

export default CardRequestFormPage