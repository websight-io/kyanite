const initMessageDelete = () => {
    document.addEventListener('DOMContentLoaded', () => {
        (document.querySelectorAll('.message .delete') || []).forEach(($delete) => {
            const $message = $delete.parentNode.parentNode;

            $delete.addEventListener('click', () => {
                $message.parentNode.removeChild($message);
            });
        });
    });
};

initMessageDelete();
