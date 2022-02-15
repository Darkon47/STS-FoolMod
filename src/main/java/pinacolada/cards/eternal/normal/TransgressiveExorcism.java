package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.cards.eternal.curse.Curse_Spite;
import pinacolada.utilities.PCLActions;

public class TransgressiveExorcism extends EternalCard
{
    public static final PCLCardData DATA = Register(TransgressiveExorcism.class).SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.None)
            .PostInitialize(data -> data.AddPreview(new Curse_Spite(), false));

    public TransgressiveExorcism()
    {
        super(DATA);

        Initialize(0, 0, 3, 1);
        SetUpgrade(0, 0, 0, 0);
        SetCostUpgrade(-1);

        SetLight();
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ExhaustFromPile(name, magicNumber, player.hand, player.discardPile)
                .ShowEffect(true, true)
                .AddCallback((cards) -> {
                    for (AbstractCard c : cards) {
                        PCLActions.Bottom.TakeDamage(secondaryValue, AbstractGameAction.AttackEffect.NONE);
                        PCLActions.Bottom.DrawNextTurn(1);
                    }
                });
    }
}