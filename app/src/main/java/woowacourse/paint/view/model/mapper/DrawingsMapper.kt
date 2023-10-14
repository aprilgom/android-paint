package woowacourse.paint.view.model.mapper

import woowacourse.paint.domain.Drawings
import woowacourse.paint.view.model.mapper.DrawingMapper.toDomain
import woowacourse.paint.view.model.mapper.DrawingMapper.toModel
import woowacourse.paint.view.model.pen.ink.Inks

object DrawingsMapper {
    fun Drawings.toModel(): Inks = value.map {
        it.toModel()
    }.let {
        Inks(it)
    }

    fun Inks.toDomain(): Drawings = value.map {
        it.toDomain()
    }.let {
        Drawings(it)
    }
}
