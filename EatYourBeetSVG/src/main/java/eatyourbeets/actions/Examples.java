package eatyourbeets.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public final class Examples extends AnimatorCard
{
    private Examples(String id, int cost, CardType type, CardRarity rarity, CardTarget target)
    {
        super(null, 0, null, null, null);
    }

    @Override
    public void upgrade() { }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        ApplyPowers(p, m);
        DealDamage(p, m);
        DrawCards(p, m);
        SelectCards(p, m);
    }

    private void ApplyPowers(AbstractPlayer p, AbstractMonster m)
    {
        // Base game implementation:
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber), magicNumber));

        // With GameActions:
        GameActions.Bottom.StackPower(new StrengthPower(p, magicNumber));
        // or:
        GameActions.Bottom.ApplyPower(p, p, new StrengthPower(p, magicNumber), magicNumber);
        // or:
        GameActions.Bottom.Add(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber), magicNumber));
        // or (this one always targets the player):
        GameActions.Bottom.GainStrength(magicNumber); // there are some more power shortcuts, like GainDexterity, GainAgility, GainForce, GainPlatedArmor etc...
    }

    private void DealDamage(AbstractPlayer p, AbstractMonster m)
    {
        // Base Game implementation:
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

        // With GameActions:
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        // or:
        GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        // or:
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.FIRE);

        // Deal Damage also has Options and Callbacks:
        GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.NONE)
        .SetOptions(true, true)
        .SetOptions2(false, false)
        .SetDamageEffect(enemy -> GameEffects.List.Add(new BiteEffect(enemy.hb_x, enemy.hb_y)))
        .AddCallback(enemy ->
        {
           GameActions.Bottom.Heal(enemy.lastDamageTaken);

           if (GameUtilities.TriggerOnKill(enemy, true))
           {
               GameActions.Bottom.GainEnergy(secondaryValue);
           }
        });
    }

    private void DrawCards(AbstractPlayer p, AbstractMonster m)
    {
        // Base Game implementation:
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, magicNumber));

        // With GameActions:
        GameActions.Bottom.Draw(magicNumber);

        // You can add options, filters, and callbacks:
        GameActions.Bottom.Draw(magicNumber)
        .SetOptions(true)
        .SetFilter(c -> !GameUtilities.IsCurseOrStatus(c), false)
        .AddCallback(m, (enemy, cards) ->
        {
           for (AbstractCard card : cards)
           {
               if (card.type == CardType.ATTACK)
               {
                   GameActions.Bottom.DealDamage(this, (AbstractMonster)enemy, AbstractGameAction.AttackEffect.FIRE);
               }
               else
               {
                   GameActions.Top.GainBlock(block);
               }
           }
        });
    }

    private void SelectCards(AbstractPlayer p, AbstractMonster m)
    {
        // Fetch an Attack from draw pile:
        // 'name' parameter is the name of the card, it is shown in square brackets at the end of the message, useful for when the card is auto played
        GameActions.Top.FetchFromPile(name, 1, p.drawPile)
        .SetFilter(c -> c.type == CardType.ATTACK);

        // Exhaust a card from draw or discard pile:
        GameActions.Top.ExhaustFromPile(name, 1, p.drawPile, p.discardPile)
        .SetMessage("Custom message here")
        .SetOptions(false, false); // AnyNumber being false means that the player cannot cancel

        // SelectFromPile does nothing other than passing the cards to a callback:
        GameActions.Top.SelectFromPile(name, 1, p.exhaustPile)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Top.MoveCard(cards.get(0), AbstractDungeon.player.drawPile, true);
            }
        });

        // Similarly select from hand needs a callback to do something:
        GameActions.Bottom.SelectFromHand(name, 2, false)
        .SetOptions(false, false, false) // If canPickLower is false and not enough cards are selectable, the callback will not be called
        .AddCallback(cards ->
        {
            for (AbstractCard card : cards)
            {
                card.baseDamage += secondaryValue;
                card.flash();
            }
        });

        // If you want to Exhaust or Discard:
        GameActions.Bottom.ExhaustFromHand(name, 1, false);
        GameActions.Bottom.DiscardFromHand(name, 1, false);
    }
}