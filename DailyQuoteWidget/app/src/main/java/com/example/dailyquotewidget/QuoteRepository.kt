package com.example.dailyquotewidget

import javax.inject.Inject

class QuoteRepository @Inject constructor(private val dao: QuoteDao) {

    suspend fun getAll(): List<Quote> = dao.getAll()

    suspend fun getRandom(): Quote? {
        val list = dao.getAll()
        if (list.isEmpty()) return null
        return list.random()
    }

    suspend fun getById(id: Int): Quote? = dao.getById(id)

    suspend fun toggleFavorite(quote: Quote) {
        dao.update(quote.copy(isFavorite = !quote.isFavorite))
    }

    suspend fun insertAll(quotes: List<Quote>) = dao.insertAll(quotes)
}
