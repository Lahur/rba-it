import type { ReactNode } from 'react'
import CardRequestFormPage from './pages/CardRequestFormPage.tsx'
import CardRequestSearchPage from './pages/CardRequestSearchPage.tsx'
import CardRequestDeletePage from './pages/CardRequestDeletePage.tsx'

export interface NavRoute {
  path: string
  label: string
  element: ReactNode
}

export const routes: NavRoute[] = [
  { path: '/card-request-form', label: 'Card Request Form', element: <CardRequestFormPage /> },
  { path: '/card-request-search', label: 'Card Request Search', element: <CardRequestSearchPage /> },
  { path: '/card-request-delete', label: 'Card Request Delete', element: <CardRequestDeletePage /> },
]