import { httpClient } from './httpClient'
import type { CardRequestRequest } from '../models/CardRequestRequest'
import type { CardRequestResponse } from '../models/CardRequestResponse'

const BASE_PATH = '/v1/card-request'

export const cardRequestService = {
    async findAll(): Promise<CardRequestResponse[]> {
        const { data } = await httpClient.get<CardRequestResponse[]>(BASE_PATH)
        return data
    },

    async findByOib(oib: string): Promise<{ data: CardRequestResponse | null; status: number }> {
        const { data, status } = await httpClient.get<CardRequestResponse>(`${BASE_PATH}/${oib}`)
        return { data: status === 204 ? null : data, status }
    },

    async save(request: CardRequestRequest): Promise<CardRequestResponse> {
        const { data } = await httpClient.post<CardRequestResponse>(BASE_PATH, request)
        return data
    },

    async deleteByOib(oib: string): Promise<void> {
        await httpClient.delete(`${BASE_PATH}/${oib}`)
    },
}