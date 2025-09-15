package com.mahesh.demo.domain.mapper

import com.google.common.truth.Truth.assertThat
import com.mahesh.demo.data.entities.holdings.Data
import com.mahesh.demo.data.entities.holdings.GetHoldingsResponse
import com.mahesh.demo.data.entities.holdings.UserHolding
import com.mahesh.demo.presentation.entities.Holdings
import org.junit.Test

class GetHoldingsResponseMapperTest {

    @Test
    fun `toHoldings maps single item correctly`() {
        val dto = GetHoldingsResponse(
            data = Data(
                userHolding = listOf(
                    UserHolding(
                        symbol = "ABC",
                        quantity = 10,
                        ltp = 100.0,
                        close = 95.0,
                        avgPrice = 90.0,
                    )
                )
            )
        )

        val result: List<Holdings> = dto.toHoldings()

        assertThat(result).hasSize(1)
        val mapped = result.first()
        assertThat(mapped.symbol).isEqualTo("ABC")
        assertThat(mapped.quantity).isEqualTo(10)
        assertThat(mapped.ltp).isEqualTo(100.0)
        assertThat(mapped.close).isEqualTo(95.0)
        assertThat(mapped.avgPrice).isEqualTo(90.0)
    }

    @Test
    fun `toHoldings returns empty list when data is null`() {
        val dto = GetHoldingsResponse(data = null)
        val result = dto.toHoldings()
        assertThat(result).isEmpty()
    }

    @Test
    fun `toHoldings returns empty list when userHolding is null`() {
        val dto = GetHoldingsResponse(data = Data(userHolding = null))
        val result = dto.toHoldings()
        assertThat(result).isEmpty()
    }

    @Test
    fun `toHoldings returns empty list when userHolding is empty`() {
        val dto = GetHoldingsResponse(data = Data(userHolding = emptyList()))
        val result = dto.toHoldings()
        assertThat(result).isEmpty()
    }
}
