import { createSlice, nanoid } from '@reduxjs/toolkit'
import type { PayloadAction } from '@reduxjs/toolkit'

export const ToastVariant = {
    SUCCESS: 'SUCCESS',
    ERROR: 'ERROR',
} as const

export type ToastVariant = (typeof ToastVariant)[keyof typeof ToastVariant]

export interface ToastItem {
    id: string
    message: string
    variant: ToastVariant
}

interface ToastState {
    toasts: ToastItem[]
}

const initialState: ToastState = {
    toasts: [],
}

const toastSlice = createSlice({
    name: 'toast',
    initialState,
    reducers: {
        showToast: {
            reducer: (state, action: PayloadAction<ToastItem>) => {
                state.toasts.push(action.payload)
            },
            prepare: (message: string, variant: ToastVariant = ToastVariant.ERROR) => ({ payload: { id: nanoid(), message, variant } }),
        },
        dismissToast: (state, action: PayloadAction<string>) => {
            state.toasts = state.toasts.filter((toast) => toast.id !== action.payload)
        },
    },
})

export const { showToast, dismissToast } = toastSlice.actions
export default toastSlice.reducer