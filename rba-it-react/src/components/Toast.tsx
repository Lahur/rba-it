import { useAppDispatch, useAppSelector } from '../app/hooks'
import { dismissToast, ToastVariant } from '../stores/toastSlice'

function Toast() {
    const dispatch = useAppDispatch()
    const toasts = useAppSelector((state) => state.toast.toasts)

    return <div className="fixed top-5 right-5 z-50 flex flex-col gap-2.5">
        {toasts.map((toast) => (
            <div key={toast.id} id="toast-default" className="flex items-center w-full max-w-xs p-4 text-body bg-neutral-primary-soft rounded-base shadow-xs border border-default" role="alert">
                {toast.variant === ToastVariant.SUCCESS ? (
                    <svg className="w-5 h-5 text-green-600" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24">
                        <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 11.917 9.724 16.5 19 7.5"/>
                    </svg>
                ) : (
                    <svg className="w-6 h-6 text-fg-brand" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24">
                        <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M18.122 17.645a7.185 7.185 0 0 1-2.656 2.495 7.06 7.06 0 0 1-3.52.853 6.617 6.617 0 0 1-3.306-.718 6.73 6.73 0 0 1-2.54-2.266c-2.672-4.57.287-8.846.887-9.668A4.448 4.448 0 0 0 8.07 6.31 4.49 4.49 0 0 0 7.997 4c1.284.965 6.43 3.258 5.525 10.631 1.496-1.136 2.7-3.046 2.846-6.216 1.43 1.061 3.985 5.462 1.754 9.23Z"/>
                    </svg>
                )}
                <div className="ms-2.5 text-sm border-s border-default ps-3.5">{toast.message}</div>
                <button type="button" onClick={() => dispatch(dismissToast(toast.id))}
                        className="ms-auto flex items-center justify-center text-body hover:text-heading bg-transparent box-border border border-transparent hover:bg-neutral-secondary-medium focus:ring-4 focus:ring-neutral-tertiary font-medium leading-5 rounded text-sm h-8 w-8 focus:outline-none"
                        aria-label="Close">
                    <span className="sr-only">Close</span>
                    <svg className="w-5 h-5" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24">
                        <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18 17.94 6M18 18 6.06 6"/>
                    </svg>
                </button>
            </div>
        ))}
    </div>
}

export default Toast