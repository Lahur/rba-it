import { useEffect } from 'react'
import { useAppDispatch, useAppSelector } from '../app/hooks'
import { clearSelected, deleteCardRequestByOib, fetchCardRequestByOib } from '../stores/cardRequestSlice'
import { showToast, ToastVariant } from '../stores/toastSlice'
import CardRequestDetailsCard from '../components/CardRequestDetailsCard'
import NoDataCard from '../components/NoDataCard'
import OibSearchForm from '../components/OibSearchForm'

function DeleteResultCard() {
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

    return <>
        <CardRequestDetailsCard cardRequest={selected} title="Card request found"/>
        <div className="max-w-sm mx-auto mt-6 p-6 bg-neutral-primary-soft border border-default rounded-base shadow-xs">
            <p className="mb-4 text-sm text-heading">Are you sure you want to delete this card request?</p>
            <div className="flex gap-3">
                <button type="button" onClick={() => dispatch(clearSelected())}
                        className="text-heading bg-transparent box-border border border-default hover:bg-neutral-secondary-medium focus:ring-4 focus:ring-neutral-tertiary font-medium leading-5 rounded-base text-sm px-4 py-2.5 focus:outline-none w-full">Cancel
                </button>
                <button type="button" onClick={handleDelete}
                        className="text-white bg-red-600 box-border border border-transparent hover:bg-red-700 focus:ring-4 focus:ring-red-300 shadow-xs font-medium leading-5 rounded-base text-sm px-4 py-2.5 focus:outline-none w-full">Yes, delete
                </button>
            </div>
        </div>
    </>
}

function CardRequestDeletePage() {
    const dispatch = useAppDispatch()

    useEffect(() => {
        dispatch(clearSelected())
    }, [dispatch])

    return <>
        <OibSearchForm onSearch={(oib) => dispatch(fetchCardRequestByOib(oib))}/>
        <DeleteResultCard/>
    </>
}

export default CardRequestDeletePage