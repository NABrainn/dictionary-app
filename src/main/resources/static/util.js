window.util = {
    /**
     * 
     * @param {{
     *      content: Node[],
     *      wrapper: Node
     * }} config 
     * @returns 
     */
    wrap: (config) => {        
        const toWrapCloned = config.content
            .map(node => node.cloneNode(true))
        const wrapperCloned = config.wrapper.cloneNode(true)
        wrapperCloned.append(...toWrapCloned)
        const firstElement = config.content.at(0)        
        if(!firstElement.parentElement){
            console.error('One of wrapped elements is not located in the DOM: ', firstElement)
            return
        }
        firstElement?.parentElement.insertBefore(wrapperCloned, firstElement)
        config.content.forEach(node => node.remove())
        return wrapperCloned
    },
    /**
     * 
     * @param {{
     *      insert: Node,
     *      before: Node
     * }} config 
     * @returns 
     */
    insertBefore: (config) => {
        if(config.before.parentElement) {
            config.before.parentElement.insertBefore(config.insert, config.before)
            return config.insert
        }
        console.error('Element to insert before must have a parent element: ', config.before)
    },
    /**
     * 
     * @param {{
     *      type: string,
     *      classList: string[],
     *      id: number
     * }} config 
     * @returns 
     */
    define: (config) => {
        const elementRef = document.createElement(config.type)
        if(config.classList) {
            elementRef.classList.add(...config.classList)
        }
        if(config.id) {
            elementRef.id = config.id
        }
        return elementRef
    },
    /**
     * 
     * @param {Element} wrapper 
     * @returns {Element[]}
     */
    unwrap: (wrapper) => {
        const children = [...wrapper.children]
        if(wrapper.children) {
            wrapper.replaceWith(...wrapper.children);
            return children
        }
        console.error('Element cannot be unwrapper: ', wrapper)
    }

    //TODO add text search API
}