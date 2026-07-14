import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import { cardRequestService } from '../services/cardRequest.service'
import type { CardRequestRequest } from '../models/CardRequestRequest'
import type { CardRequestResponse } from '../models/CardRequestResponse'

interface CardRequestState {
    items: CardRequestResponse[]
    selected: CardRequestResponse | null
    searchedNotFound: boolean
}

const initialState: CardRequestState = {
    items: [],
    selected: null,
    searchedNotFound: false,
}

export const fetchCardRequestByOib = createAsyncThunk(
    'cardRequest/fetchByOib',
    (oib: string) => cardRequestService.findByOib(oib),
)

export const createCardRequest = createAsyncThunk(
    'cardRequest/create',
    (request: CardRequestRequest) => cardRequestService.save(request),
)

export const deleteCardRequestByOib = createAsyncThunk(
    'cardRequest/deleteByOib',
    async (oib: string) => {
        await cardRequestService.deleteByOib(oib)
        return oib
    },
)

const cardRequestSlice = createSlice({
    name: 'cardRequest',
    initialState,
    reducers: {
        clearSelected: (state) => {
            state.selected = null
            state.searchedNotFound = false
        },
    },
    extraReducers: (builder) => {
        builder
            .addCase(fetchCardRequestByOib.fulfilled, (state, action) => {
                state.selected = action.payload.data
                state.searchedNotFound = action.payload.status === 204
            })
            .addCase(createCardRequest.fulfilled, (state, action) => {
                state.selected = action.payload
            })
            .addCase(deleteCardRequestByOib.fulfilled, (state, action) => {
                if (state.selected?.oib === action.payload) {
                    state.selected = null
                }
            })
    },
})

export const { clearSelected } = cardRequestSlice.actions
export default cardRequestSlice.reducer