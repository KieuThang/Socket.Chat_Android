package com.github.kieuthang.login_chat.views.login


import android.content.Context
import com.github.kieuthang.login_chat.data.user.entity.AccessToken

import com.github.kieuthang.login_chat.data.user.entity.BaseResponseModel
import com.github.kieuthang.login_chat.data.user.entity.UserResponseModel
import com.github.kieuthang.login_chat.domain.interactor.DefaultSubscriber
import com.github.kieuthang.login_chat.domain.interactor.UserUseCase
import com.github.kieuthang.login_chat.views.common.BaseContract

class LoginPresenter internal constructor(context: Context) : BaseContract.Presenter<ILoginDataLoadView> {
    private var iLoginDataLoadView: ILoginDataLoadView? = null
    private val mUserUseCase: UserUseCase = UserUseCase(context)

    override fun bindView(view: ILoginDataLoadView) {
        iLoginDataLoadView = view
    }

    override fun release() {

    }

    fun login(email: String, password: String) {
        iLoginDataLoadView!!.showLoading()
        mUserUseCase.execute(UserUseCase.LoginUser(email, password), object : DefaultSubscriber<AccessToken>() {
            override fun onNext(t: AccessToken) {
                super.onNext(t)

                iLoginDataLoadView!!.onLoginResult(t)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                iLoginDataLoadView!!.hideLoading()
                iLoginDataLoadView!!.onLoginResult(null)
            }
        })
    }

    internal fun forgotPW(email: String) {
        iLoginDataLoadView!!.showLoading()
        mUserUseCase.execute(UserUseCase.ForgotPW(email), object : DefaultSubscriber<BaseResponseModel>() {
            override fun onNext(t: BaseResponseModel) {
                super.onNext(t)
                iLoginDataLoadView!!.hideLoading()
                iLoginDataLoadView!!.onForgotPWResult(t)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                iLoginDataLoadView!!.hideLoading()
                iLoginDataLoadView!!.onForgotPWResult(null)
            }
        })
    }

    internal fun getMyProfile(isPullToRefresh: Boolean) {
        mUserUseCase.execute(UserUseCase.GetMyProfile(isPullToRefresh), object : DefaultSubscriber<UserResponseModel>() {
            override fun onNext(t: UserResponseModel) {
                super.onNext(t)
                iLoginDataLoadView!!.hideLoading()
                iLoginDataLoadView!!.onGetMyProfileResult(t, null)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                iLoginDataLoadView!!.hideLoading()
                iLoginDataLoadView!!.onGetMyProfileResult(null, e)
            }
        })
    }
}
