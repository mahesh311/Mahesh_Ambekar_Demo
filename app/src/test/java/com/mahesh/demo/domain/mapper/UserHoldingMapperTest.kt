package com.mahesh.demo.domain.mapper

import com.google.common.truth.Truth.assertThat
import com.mahesh.demo.data.entities.holdings.UserHolding
import com.mahesh.demo.data.entities.holdings.toHoldings
import com.mahesh.demo.presentation.entities.Holdings
import org.junit.Test

class UserHoldingMapperTest {

    @Test
    fun `maps fully populated UserHolding correctly`() {
        val dto = UserHolding(
            symbol = "ABC",
            quantity = 10,
            ltp = 100.0,
            avgPrice = 90.0,
            close = 95.0
        )

        val result: Holdings = dto.toHoldings()

        assertThat(result.symbol).isEqualTo("ABC")
        assertThat(result.quantity).isEqualTo(10)
        assertThat(result.ltp).isEqualTo(100.0)
        assertThat(result.avgPrice).isEqualTo(90.0)
        assertThat(result.close).isEqualTo(95.0)
        assertThat(result.totalPnl).isEqualTo((100.0 * 10) - (90.0 * 10))
        assertThat(result.todayPnl).isEqualTo(0.0)
    }

    @Test
    fun `maps with null values defaults to safe values`() {
        val dto = UserHolding(
            symbol = null,
            quantity = null,
            ltp = null,
            avgPrice = null,
            close = null
        )

        val result: Holdings = dto.toHoldings()

        assertThat(result.symbol).isEqualTo("")
        assertThat(result.quantity).isEqualTo(0)
        assertThat(result.ltp).isEqualTo(0.0)
        assertThat(result.avgPrice).isEqualTo(0.0)
        assertThat(result.close).isEqualTo(0.0)
        assertThat(result.totalPnl).isEqualTo(0.0)
        assertThat(result.todayPnl).isEqualTo(0.0)
    }

    @Test
    fun `computes totalPnl correctly`() {
        val dto = UserHolding(
            symbol = "XYZ",
            quantity = 5,
            ltp = 200.0,
            avgPrice = 150.0,
            close = 210.0
        )

        val result: Holdings = dto.toHoldings()

        val expectedTotalPnl = (200.0 * 5) - (150.0 * 5)
        assertThat(result.totalPnl).isEqualTo(expectedTotalPnl)
    }
}
