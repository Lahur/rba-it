import { useEffect } from 'react'
import { useAppDispatch, useAppSelector } from '../app/hooks'
import { clearSelected, deleteCardRequestByOib, fetchCardRequestByOib } from '../stores/cardRequestSlice'
import { showToast, ToastVariant } from '../stores/toastSlice'
import CardRequestDetailsCard from '../components/CardRequestDetailsCard'
import NoDataCard from '../components/NoDataCard'
import OibSearchForm from '../components/OibSearchForm'

function SearchResultCard() {
    const dispatch = useAppDispatch()
    const selected = useAppSelector((state) => state.cardRequest.selected)
    const searchedNotFound = useAppSelector((state) => state.cardRequest.searchedNotFound)

    if (searchedNotFound) {
        return <NoDataCard/>
    }

    if (!selected) {
        return null
    }

    const handleDelete = async () => {
        try {
            await dispatch(deleteCardRequestByOib(selected.oib)).unwrap()
            dispatch(showToast('Card request deleted successfully', ToastVariant.SUCCESS))
        } catch {
            // error toast is shown by the http client interceptor
        }
    }

    return <CardRequestDetailsCard cardRequest={selected} title="Card request found" headerAction={
        <button type="button" onClick={handleDelete}
                className="ms-auto flex items-center justify-center text-body hover:text-red-600 bg-transparent box-border border border-transparent hover:bg-neutral-tertiary rounded-base h-8 w-8 focus:outline-none focus:ring-4 focus:ring-neutral-tertiary"
                aria-label="Delete">
            <svg className="w-5 h-5" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24">
                <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 7h14M10 11v6M14 11v6M6 7l1 12a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2l1-12M9 7V4a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v3"/>
            </svg>
        </button>
    }/>
}

function CardRequestSearchPage() {
    const dispatch = useAppDispatch()

    useEffect(() => {
        dispatch(clearSelected())
    }, [dispatch])

    return <>
        <OibSearchForm onSearch={(oib) => dispatch(fetchCardRequestByOib(oib))}/>
        <SearchResultCard/>
    </>
}

export default CardRequestSearchPage
