import { configureStore } from '@reduxjs/toolkit'
import cardRequestReducer from '../stores/cardRequestSlice'
import toastReducer, { showToast } from '../stores/toastSlice'
import { httpClient } from '../services/httpClient'

export const store = configureStore({
  reducer: {
    cardRequest: cardRequestReducer,
    toast: toastReducer,
  },
})

httpClient.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 400) {
            const fieldErrors: { defaultMessage?: string }[] | undefined = error.response?.data?.errors
            if (fieldErrors && fieldErrors.length > 0) {
                fieldErrors.forEach((fieldError) => {
                    store.dispatch(showToast(fieldError.defaultMessage ?? 'Validation failed'))
                })
            } else {
                store.dispatch(showToast(error.response?.data?.message ?? 'Bad request'))
            }
        } else {
            store.dispatch(showToast('Something went wrong. Please try again later.'))
        }
        return Promise.reject(error)
    },
)

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch