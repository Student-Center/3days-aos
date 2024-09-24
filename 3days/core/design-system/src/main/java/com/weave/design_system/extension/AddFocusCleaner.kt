package com.weave.design_system.extension

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput

/**
 * 컴포저블에 포커스 해제 동작을 추가하는 확장 함수.
 *
 * 이 Modifier는 컴포저블에 탭 제스처 감지기를 추가하여 탭할 때 제공된 [FocusManager]를 사용해
 * 현재 포커스를 해제합니다. 또한, [doOnClear] 람다를 전달하여 포커스 해제 시 추가적인 동작을 정의할 수 있습니다.
 *
 * @param focusManager 현재 포커스된 컴포넌트의 포커스를 해제할 때 사용하는 [FocusManager].
 * @param doOnClear 포커스를 해제할 때 실행되는 선택적인 람다 함수.
 *
 * @return 포커스 해제 동작이 포함된 Modifier.
 */
fun Modifier.addFocusCleaner(focusManager: FocusManager, doOnClear: () -> Unit = {}): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(onTap = {
            doOnClear()
            focusManager.clearFocus()
        })
    }
}
