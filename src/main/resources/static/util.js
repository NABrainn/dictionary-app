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
        if(!config) 
            throw new Error('config object cannot be undefined')   
        if(!config.content) 
            throw new Error('config property "content" cannot be undefined')
        if(!config.wrapper) 
            throw new Error('config property "wrapper" cannot be undefined')
        const toWrapCloned = config.content.map(node => node.cloneNode(true))
        toWrapCloned.forEach(node => { node.dataset.isWrapped = 'true' })
        const wrapperCloned = config.wrapper.cloneNode(true)
        wrapperCloned.append(...toWrapCloned)
        const firstElement = config.content.at(0)        
        if(!firstElement) {
            throw new Error('One of wrapped elements is not located in the DOM')
        }
        firstElement.parentElement.insertBefore(wrapperCloned, firstElement)
        config.content.forEach(node => node.remove())
        return { wrapper: wrapperCloned, content: toWrapCloned}
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
        if(config) {
            if(config.before && config.insert) {
                config.before.parentElement.insertBefore(config.insert, config.before)
                return config.insert
            }
        }
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
        if (config.data && config.data.length) {
            config.data.forEach(entry => {
                const camelCaseKey = entry.key.replace(/-([a-z])/g, (match, letter) => letter.toUpperCase());
                elementRef.dataset[camelCaseKey] = entry.value;
            });
        }
        return elementRef
    },
    /**
     * 
     * @param {Element} element 
     */
    removeInnerHTML: (element) => {
        if(!element) throw new Error('Element is undefined')
        element.innerHTML = ''
    },
    /**
     * 
     * @param {{
     *    key: string,
     *    value: string
     * }} data 
     * @returns {Node}
     */
    findByData: (data) => {
        return htmx.find(`[data-${data.key}="${data.value}"]`)
    },
    /**
     * 
     * @param {{
     *    key: string,
     *    value: string
     * }} data 
     * @returns {Node[]}
     */
    findAllByData: (data) => {
        return htmx.findAll(`[data-${data.key}="${data.value}"]`)
    },
    /**
     * 
     * @param {{
     *    node: Node,
     *    toRemove: string[],
     *    toAdd: string[]
     * }} config 
     */
    replaceClasses: (config) => {
        if (!config) throw new Error('Passed parameter is undefined');
        if (!config.node || !('classList' in config.node)) {
            throw new Error('Passed node is not a valid DOM element: ', config.node);
        }
        
        if(Array.isArray(config.toRemove)) {
            config.node.classList.remove(...config.toRemove)
        }
        else {
            config.node.classList.remove(config.toRemove)
        }

        if(Array.isArray(config.toAdd)) {
            config.node.classList.add(...config.toAdd)
        }
        else {
            config.node.classList.add(config.toAdd)
        }
    },
    /**
     * 
     * @param {Element} wrapper 
     * @returns {Element[]}
     */
    unwrap: (wrapper) => {
        const children = [...wrapper.children]
        if(children) {
            children.forEach(child => { child.dataset.isWrapped = 'false' })
            wrapper.replaceWith(...children);
            return children
        }
        console.error('Element cannot be unwrapped: ', wrapper)
    },

    /**
     * 
     * @param {{
     *    nodes: Node[],
     *    text: string
     * }} config 
     * @returns {Node[][]}
     */
    findAllByText: (config) => {       
        if(!config) {
            throw new Error('Config cannot be undefined')
        } 
        if(!config.text) {
            throw new Error('Text config cannot be undefined')
        }
        if(!config.nodes) {
            throw new Error('Nodes config cannot be undefined')
        }
        
        const searchWords = config.text
            .toLowerCase()
            .replace(/[^\p{L}\p{N}\s]/gu, '')
            .split(' ');            
        const phraseWordNodes = config.nodes
            .map((node, index) => ({
                id: index,
                text: node
                    ?.innerText
                    ?.replace(/[^\p{L}\p{N}\s]/gu, '')
                    ?.toLowerCase() ?? '',
                node: node
            }))
            .filter(node => searchWords.includes(node.text));        
        
        const phraseNodes = []
        let buffer = [];
        let pointer = 0;
        for(const node of phraseWordNodes) {
            //check if elements are subsequent
            const lastInBuffer = buffer.at(buffer.length - 1)
            if(lastInBuffer) {
                if(node.id - lastInBuffer.id !== 1) {
                    buffer = []
                    pointer = 0
                }
            }
            //check if subsequent elements match searched phrase
            if(node.text !== searchWords.at(pointer)) {
                buffer = []
                pointer = 0
            }
            buffer.push(node)
            pointer++
            //check if buffer is filled up
            if(buffer.map(word => word.text).join(' ') === searchWords.join(' ')) {                                                
                phraseNodes.push(buffer)
                buffer = []
                pointer = 0
            }
        }
        
        return phraseNodes
            .map(arr => arr.map(item => item.node))
    },

    
    catchErr(callback) {
        try {
            const val = callback();
            return { ok: true, value: val };
        } catch (e) {
            return { ok: false, err: e };
        }
    },

    /**
     * 
     * @param {{ 
     * element: Element, 
     * method: { key: string, value: string }, 
     * target: string, 
     * swap: string, 
     * trigger: string, 
     * vals: any 
     * }} config 
     * @returns 
     */
    setHxAttributes(config) {
        if(!config.element) {
            return
        }
        if(!config.method) {
            return
        }
        if(!config.method.key || !config.method.value) {
            return
        }
        config.element.setAttribute(config.method.key, config.method.value)

        if(config.target) {
            config.element.setAttribute("hx-target", config.target)
        }
        if(config.swap) {
            config.element.setAttribute("hx-swap", config.swap)
        }
        if(config.trigger) {
            config.element.setAttribute("hx-trigger", config.trigger)
        }
        if (config.vals) {
            const valsString = typeof config.vals === 'object' ? JSON.stringify(config.vals) : config.vals;
            config.element.setAttribute("hx-vals", valsString);
        }
        if(config.beforeRequest) {
            config.element.setAttribute("hx-on::before-request", config.beforeRequest.toString())
        }
        htmx.process(config.element)
    }
}