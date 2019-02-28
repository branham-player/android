package com.branhamplayer.android.di

import android.app.Activity
import android.content.Context

class DaggerInjector {
    companion object {

        // region Authentication

        var authenticationComponent: AuthenticationComponent? = null
            private set


        fun buildAuthenticationComponent(activity: Activity): AuthenticationComponent {
            val component = authenticationComponent ?: DaggerAuthenticationComponent
                .builder()
                .authenticationModule(AuthenticationModule(activity))
                .build()

            authenticationComponent = component
            return component
        }

        // endregion

        // region Middleware

        var middlewareComponent: MiddlewareComponent? = null
            private set


        fun buildMiddlewareComponent(activity: Activity): MiddlewareComponent {
            val component = middlewareComponent ?: DaggerMiddlewareComponent
                .builder()
                .middlewareModule(MiddlewareModule(activity))
                .build()

            middlewareComponent = component
            return component
        }

        // endregion

        // region Routing

        var routingComponent: RoutingComponent? = null
            private set


        fun buildRoutingComponent(context: Context): RoutingComponent {
            val component = routingComponent ?: DaggerRoutingComponent
                .builder()
                .routingModule(RoutingModule(context))
                .build()

            routingComponent = component
            return component
        }

        // endregion
    }
}
